package com.jlu.pipeline.job.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;

import com.jlu.common.interceptor.UserLoginHelper;
import com.jlu.plugin.thread.PluginTask;
import com.jlu.plugin.thread.PluginThreadService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jlu.common.aop.utils.AopTargetUtils;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.CollUtils;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.runtime.RuntimeRequire;
import com.jlu.plugin.runtime.bean.RunTimeBean;
import com.jlu.plugin.runtime.service.PluginValueGenerator;
import com.jlu.plugin.service.IPluginInfoService;

/**
 * Created by Administrator on 2018/1/18.
 */
@Service
public class JobBuildServiceImpl implements IJobBuildService, ApplicationContextAware {

    private static final String SERVER_BUSY = "系统繁忙，请稍后重试";
    private ApplicationContext applicationContext;

    @Autowired
    private IPluginInfoService pluginInfoService;

    @Autowired
    private IJobBuildDao jobBuildDao;

    @Autowired
    private IPipelineBuildDao pipelineBuildDao;

    @Autowired
    private PluginThreadService pluginThreadService;

    @Override
    public Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId,
                          Map<String, String> params) {
        JobBuild jobBuild = new JobBuild();
        jobBuild.setJobConfId(jobConfBean.getId());
        jobBuild.setUpStreamJobBuildId(upStreamJobBuildId);
        jobBuild.setPipelineBuildId(pipelineBuildId);
        jobBuild.setJobStatus(PipelineJobStatus.INIT);
        jobBuild.setName(jobConfBean.getName());
        jobBuild.setTriggerMode(jobConfBean.getTriggerMode());
        Map<String, String> confParam = jobConfBean.getParameterMap();
        // 系统参数优于用户自定义参数
        Map<String, String> mergedParam = CollUtils.merge(params, confParam);
        jobBuild.setInParams(JSON.toJSONString(mergedParam));
        PluginType pluginType = jobConfBean.getPluginType();
        jobBuild.setPluginType(pluginType);
        Long pluginBuildId = pluginInfoService.getRealJobPlugin(pluginType).getDataOperator().initPluginBuildByPluginConf(jobConfBean.getPluginConfId());
        jobBuild.setPluginBuildId(pluginBuildId);

        jobBuildDao.save(jobBuild);
        return jobBuild.getId();
    }

    @Override
    public void build(Long jobBuildId, Map<String, String> execParam, Map<String, Object> runtimeJobParam, TriggerMode triggerMode, String triggerUser) {
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        // TODO: 2018/3/20
        if (jobBuild == null) {
            throw new PipelineRuntimeException("TODO");
        }
        jobBuild.setTriggerTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuild.setTriggerUser(triggerUser);
        jobBuild.setTriggerMode(triggerMode);
        jobBuild.setMessage(StringUtils.EMPTY);
        jobBuildDao.saveOrUpdate(jobBuild);
        // 如果是头job，则更新流水线状态
        if (jobBuild.getUpStreamJobBuildId() == 0L) {
            updatePipelineBuildStart(jobBuild.getPipelineBuildId(), PipelineJobStatus.RUNNING);
        }
        Long pipelineBuildId = jobBuild.getPipelineBuildId();
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, execParam, runtimeJobParam);
        PluginTask pluginTask = new PluginTask(jobBuildContext, jobBuild);
        try {
            pluginThreadService.execute(pluginTask);
        } catch (RejectedExecutionException re) {
            if (triggerMode.equals(TriggerMode.AUTO)) {
                notifiedJobBuildBusy(jobBuild);
            } else {
                throw new PipelineRuntimeException(SERVER_BUSY);
            }
        }
    }

    private void notifiedJobBuildBusy(JobBuild jobBuild) {
        jobBuild.setJobStatus(PipelineJobStatus.FAILED);
        jobBuild.setMessage(SERVER_BUSY);
        notifiedJobBuildFinished(jobBuild, null);
    }

    @Override
    public void buildTopJob(Long pipelineBuildId, TriggerMode triggerMode, String triggerUser) {
        JobBuild jobBuild = jobBuildDao.getTopJob(pipelineBuildId);
        if (jobBuild == null) {
            throw new PipelineRuntimeException("未找到job");
        }

        // 手动则不执行
        if (TriggerMode.MANUAL.equals(jobBuild.getTriggerMode())) {
            return;
        }
        updatePipelineBuildStart(pipelineBuildId, PipelineJobStatus.RUNNING);
        jobBuild.setTriggerUser(triggerUser);
        jobBuild.setTriggerMode(triggerMode);
        jobBuild.setTriggerTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuild.setMessage(StringUtils.EMPTY);
        jobBuildDao.saveOrUpdate(jobBuild);
        Map<String, Object> runtimeParamMap = new HashedMap();
        List<RunTimeBean> runTimeBeanList = getRuntimeRequire(jobBuild.getId());
        for (RunTimeBean runTimeBean : runTimeBeanList) {
            runtimeParamMap.put(runTimeBean.getName(), runTimeBean.getDefaultValue());
        }
        // 自动执行的job无用户自定义参数
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, new HashMap<>(), new HashMap<>());
        PluginTask pluginTask = new PluginTask(jobBuildContext, jobBuild);
        try {
            pluginThreadService.execute(pluginTask);
        } catch (RejectedExecutionException re) {
            notifiedJobBuildBusy(jobBuild);
        }
    }

    private void updatePipelineBuildStart(Long pipelineBuildId, PipelineJobStatus pipelineStatus) {
        PipelineBuild pipelineBuild = pipelineBuildDao.findById(pipelineBuildId);
        pipelineBuild.setStartTime(new Date());
        pipelineBuild.setPipelineStatus(pipelineStatus);
        pipelineBuildDao.saveOrUpdate(pipelineBuild);
    }

    private void updatePipelineBuildEnd(Long pipelineBuildId, PipelineJobStatus pipelineStatus) {
        PipelineBuild pipelineBuild = pipelineBuildDao.findById(pipelineBuildId);
        pipelineBuild.setEndTime(new Date());
        pipelineBuild.setPipelineStatus(pipelineStatus);
        pipelineBuildDao.saveOrUpdate(pipelineBuild);
    }

    private JobBuildContext initJobBuildContext(Long pipelineBuildId, JobBuild jobBuild,
                                                Map<String, String> execParams, Map<String, Object> runtimePluginParam) {
        JobBuildContext jobBuildContext = new JobBuildContext();
        PipelineBuild pipelineBuild = pipelineBuildDao.findById(pipelineBuildId);
        jobBuildContext.setPipelineBuild(pipelineBuild);
        jobBuildContext.setJobExecParam(execParams);
        jobBuildContext.setRuntimePluginParam(runtimePluginParam);
        // 处理jobBuild的参数
        Map<String, String> originParams = jobBuild.getInParameterMap();
        Map<String, String> newParams = CollUtils.merge(execParams, originParams);
        String paramStr = JSON.toJSONString(newParams);
        jobBuild.setInParams(paramStr);
        jobBuildDao.saveOrUpdate(jobBuild);
        return jobBuildContext;
    }

    @Override
    public boolean cancel(Long jobBuildId) {
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        // 检查是否在队列阻塞中
        boolean isInQueue = pluginThreadService.removeInQueue(new PluginTask(jobBuild));
        if (isInQueue) {
            notifiedJobBuildStartCanceled(jobBuild);
            return true;
        }
        Thread jobThread = pluginThreadService.getJobBuildThread(jobBuildId);
        // 如果仍然在线程执行中,发出中断请求
        if (jobThread != null) {
            jobThread.interrupt();
            return false;
        }
        if (jobBuild == null) {
            throw new PipelineRuntimeException("无此Job记录");
        }
        if (!PipelineJobStatus.RUNNING.equals(jobBuild.getJobStatus())) {
            throw new PipelineRuntimeException("该Job未处于运行中,无法取消");
        }
        PluginType pluginType = jobBuild.getPluginType();
        // 如果已经调起，则执行Job的取消方法
        pluginInfoService.getRealJobPlugin(pluginType).getExecutor().cancel(jobBuild);
        return true;
    }

    @Override
    public void notifiedJobBuildStarFailed(JobBuild jobBuild) {
        jobBuild.setEndTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.FAILED);
        jobBuildDao.saveOrUpdate(jobBuild);
    }

    @Override
    public void notifiedJobBuildStartCanceled(JobBuild jobBuild) {
        jobBuild.setStartTime(null);
        jobBuild.setTriggerUser(null);
        // TODO CANCELED
        jobBuild.setJobStatus(PipelineJobStatus.INIT);
        jobBuild.setTriggerTime(null);
        jobBuildDao.saveOrUpdate(jobBuild);
    }

    @Override
    public void notifiedJobBuildCanceled(JobBuild jobBuild) {
        jobBuild.setMessage("任务被取消");
        jobBuild.setEndTime(new Date());
        // TODO CANCELED
        jobBuild.setJobStatus(PipelineJobStatus.FAILED);
        jobBuildDao.saveOrUpdate(jobBuild);
    }

    @Override
    public void notifiedJobBuildFinished(JobBuild jobBuild, Map<String, String> newOutParams) {
        // 保存job状态，以及参数
        Map<String, String> outParams = CollUtils.merge(newOutParams, jobBuild.getInParameterMap());
        jobBuild.setOutParams(JSON.toJSONString(outParams));
        jobBuild.setEndTime(new Date());
        jobBuildDao.saveOrUpdate(jobBuild);
        // 更新下游job参数
        Long upStreamJobBuildId = jobBuild.getId();
        Map<String, String> upStreamJobOutParams = jobBuild.getOutParameterMap();
        JobBuild lowStreamJobBuild = jobBuildDao.getByUpStreamJobBuildId(upStreamJobBuildId);
        if (lowStreamJobBuild == null) {
            // 流水线结束
            updatePipelineBuildEnd(jobBuild.getPipelineBuildId(), jobBuild.getJobStatus());
            return;
        }

        Map<String, String> originParams = lowStreamJobBuild.getInParameterMap();
        Map<String, String> newParams = CollUtils.merge(upStreamJobOutParams, originParams);
        lowStreamJobBuild.setInParams(JSON.toJSONString(newParams));
        jobBuildDao.saveOrUpdate(lowStreamJobBuild);
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.FAILED)) {
            updatePipelineBuildEnd(jobBuild.getPipelineBuildId(), jobBuild.getJobStatus());
        }
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)
                && TriggerMode.AUTO.equals(lowStreamJobBuild.getTriggerMode())) {
            // 如果下一个job是自动，则继续构建
            Map<String, Object> runtimeParamMap = new HashedMap();
            List<RunTimeBean> runTimeBeanList = getRuntimeRequire(jobBuild.getId());
            for (RunTimeBean runTimeBean : runTimeBeanList) {
                runtimeParamMap.put(runTimeBean.getName(), runTimeBean.getDefaultValue());
            }
            build(lowStreamJobBuild.getId(), new HashedMap(), runtimeParamMap, TriggerMode.AUTO,
                    jobBuild.getTriggerUser());
        }
    }

    @Override
    public List<JobBuildBean> getJobBuildBeans(Long pipelineBuildId) {
        List<JobBuildBean> jobBuildBeanList = new LinkedList<>();
        List<JobBuild> jobBuilds = jobBuildDao.getByPipelineBuildId(pipelineBuildId);
        PipelineJobStatus lastJobStatus = PipelineJobStatus.SUCCESS;
        for (JobBuild jobBuild : jobBuilds) {
            JobBuildBean jobBuildBean = new JobBuildBean();
            // 上一个job是成功的，当前job是未执行的时候，才能执行
            if (PipelineJobStatus.INIT.equals(jobBuild.getJobStatus()) && lastJobStatus.equals(PipelineJobStatus.SUCCESS)) {
                jobBuildBean.setBuildable(true);
            }
            BeanUtils.copyProperties(jobBuild, jobBuildBean);
            jobBuildBeanList.add(jobBuildBean);
            lastJobStatus = jobBuild.getJobStatus();
        }
        // jobBuild是按照id从小到大的顺序排列的,此处查询出来也是如此，不需要再次排序
        return jobBuildBeanList;
    }

    @Override
    public void saveOrUpdate(JobBuild jobBuild) {
        jobBuildDao.saveOrUpdate(jobBuild);
    }

    @Override
    public JobBuildBean getWithPluginBuild(Long jobBuildId) {
        JobBuildBean jobBuildBean = new JobBuildBean();
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        if (jobBuild == null) {
            return null;
        }
        BeanUtils.copyProperties(jobBuild, jobBuildBean);
        Object pluginBuild = pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getDataOperator()
                .getBuild(jobBuild.getPluginBuildId());
        Long upStreamBuildId = jobBuild.getUpStreamJobBuildId();
        PipelineJobStatus upStreamJobStatus;
        if (upStreamBuildId == 0L) {
            upStreamJobStatus = PipelineJobStatus.SUCCESS;
        } else {
            JobBuild upJobBuild = jobBuildDao.findById(upStreamBuildId);
            upStreamJobStatus = upJobBuild == null ? PipelineJobStatus.FAILED : upJobBuild.getJobStatus();
        }
        if (PipelineJobStatus.SUCCESS.equals(upStreamJobStatus) && PipelineJobStatus.INIT.equals(jobBuild.getJobStatus())) {
            jobBuildBean.setBuildable(true);
        }
        jobBuildBean.setPluginBuild(pluginBuild);
        return jobBuildBean;
    }

    @Override
    public List<RunTimeBean> getRuntimeRequire(Long jobBuildId) {
        List<RunTimeBean> runTimeBeanList = new ArrayList<>();
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        if (jobBuild == null) {
            return runTimeBeanList;
        }
        Field[] fields = pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getDataOperator().getBuild(jobBuild.getPluginBuildId()).getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field filed = fields[i];
            RuntimeRequire runtimeRequire = filed.getAnnotation(RuntimeRequire.class);
            if (runtimeRequire != null) {
                Class defaultValueClass = runtimeRequire.defaultValueClass();
                try {
                    PluginValueGenerator pluginValueGenerator = (PluginValueGenerator) applicationContext.getBean(defaultValueClass);
                    Object value = pluginValueGenerator.generator(jobBuild);
                    RunTimeBean runTimeBean = new RunTimeBean();
                    runTimeBean.setName(filed.getName())
                            .setDefaultValue(value)
                            .setDescription(runtimeRequire.description())
                            .setFormType(runtimeRequire.formType().getType())
                            .setCheckRegex(runtimeRequire.checkRegex());
                    runTimeBeanList.add(runTimeBean);
                } catch (Exception e) {
                    // TODO log
                    continue;
                }
            }
        }
        return runTimeBeanList;
    }

    @Override
    public JobBuild getLastSuccBuild(String module, String commitId, PluginType pluginType, Long currentPipelineBuildId) {
        List<PipelineBuild> pipelineBuilds = pipelineBuildDao.get(module, commitId);
        if (CollUtils.isEmpty(pipelineBuilds)) {
            return null;
        }
        for (PipelineBuild pipelineBuild : pipelineBuilds) {
            Long pipelineBuildId = pipelineBuild.getId();
            if (pipelineBuildId.equals(currentPipelineBuildId)) {
                continue;
            }
            JobBuild jobBuild = jobBuildDao.getLastBuild(pipelineBuildId, pluginType, PipelineJobStatus.SUCCESS);
            if (jobBuild == null) {
                continue;
            }
            return jobBuild;
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
