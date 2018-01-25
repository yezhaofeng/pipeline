package com.jlu.pipeline.job.service.impl;

import java.lang.reflect.Field;
import java.util.*;

import com.jlu.common.utils.AopTargetUtils;
import com.jlu.pipeline.job.bean.*;
import com.jlu.plugin.runtime.service.PluginValueGenerator;
import com.jlu.plugin.runtime.bean.RunTimeBean;
import com.jlu.plugin.runtime.RuntimeRequire;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.CollUtils;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.service.IPluginInfoService;

/**
 * Created by Administrator on 2018/1/18.
 */
@Service
public class JobBuildServiceImpl implements IJobBuildService, ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Autowired
    private IPluginInfoService pluginInfoService;

    @Autowired
    private IJobBuildDao jobBuildDao;

    @Autowired
    private IPipelineBuildDao pipelineBuildDao;

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
        if (jobBuild == null) {
            throw new PipelineRuntimeException("TODO");
        }
        jobBuild.setTriggerTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuild.setTriggerUser(triggerUser);
        jobBuild.setTriggerMode(triggerMode);
        jobBuildDao.saveOrUpdate(jobBuild);
        // 如果是头job，则更新流水线状态
        if (jobBuild.getUpStreamJobBuildId() == 0L) {
            updatePipelineBuildStart(jobBuild.getPipelineBuildId(), PipelineJobStatus.RUNNING);
        }
        Long pipelineBuildId = jobBuild.getPipelineBuildId();
        PluginType pluginType = jobBuild.getPluginType();
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, execParam, runtimeJobParam);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((IPluginInfoService) AopTargetUtils.getTarget(pluginInfoService)).getRealJobPlugin(pluginType).getExecutor().executeJob(jobBuildContext, jobBuild);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        jobBuildDao.saveOrUpdate(jobBuild);
        PluginType pluginType = jobBuild.getPluginType();
        // 自动执行的job无用户自定义参数
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, new HashMap<>(), new HashMap<>());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ((IPluginInfoService) AopTargetUtils.getTarget(pluginInfoService)).getRealJobPlugin(pluginType).getExecutor().executeJob(jobBuildContext, jobBuild);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    public void notifiedJobBuildUpdated(JobBuild jobBuild, Map<String, String> newOutParams) {
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
            IJobBuildService jobBuildService = this;
            build(lowStreamJobBuild.getId(), new HashedMap(), new HashMap<>(), TriggerMode.AUTO, jobBuild.getTriggerUser());
        }
    }

    @Override
    public List<JobBuildBean> getJobBuildBeans(Long pipelineBuildId) {
        List<JobBuildBean> jobBuildBeanList = new LinkedList<>();
        List<JobBuild> jobBuilds = jobBuildDao.getByPipelineBuildId(pipelineBuildId);
        for (JobBuild jobBuild : jobBuilds) {
            JobBuildBean jobBuildBean = new JobBuildBean();
            BeanUtils.copyProperties(jobBuild, jobBuildBean);
            jobBuildBeanList.add(jobBuildBean);
        }
        // jobBuild是按照id从小到大的顺序排列的,此处查询出来也是如此，不需要再次排序
        return jobBuildBeanList;
    }

    @Override
    public void saveOrUpdate(JobBuild jobBuild) {
        jobBuildDao.saveOrUpdate(jobBuild);
    }

    @Override
    public JobBuildBean getBuildInfo(Long jobBuildId) {
        JobBuildBean jobBuildBean = new JobBuildBean();
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        BeanUtils.copyProperties(jobBuild, jobBuildBean);
        Object pluginBuild = pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getDataOperator()
                .getBuild(jobBuild.getPipelineBuildId());

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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
