package com.jlu.pipeline.job.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.common.utils.MapUtils;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.bean.TriggerMode;
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
public class JobBuildServiceImpl implements IJobBuildService {

    @Autowired
    private IPluginInfoService pluginInfoService;

    @Autowired
    private IJobBuildDao jobBuildDao;

    @Autowired
    private IPipelineBuildDao pipelineBuildDao;

    @Override
    public Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId,
                          Map<String, Object> params) {
        JobBuild jobBuild = new JobBuild();
        jobBuild.setJobConfId(jobConfBean.getId());
        jobBuild.setUpStreamJobBuildId(upStreamJobBuildId);
        jobBuild.setPipelineBuildId(pipelineBuildId);
        jobBuild.setJobStatus(PipelineJobStatus.INIT);
        jobBuild.setName(jobConfBean.getName());
        Map<String, Object> confParam = jobConfBean.getParameterMap();
        // 系统参数优于用户自定义参数
        Map<String, Object> mergedParam = MapUtils.merge(confParam, params);
        jobBuild.setInParams(JSON.toJSONString(mergedParam));
        PluginType pluginType = jobConfBean.getPluginType();
        jobBuild.setPluginType(pluginType);
        Long pluginBuildId = pluginInfoService.getRealJobPlugin(pluginType).getDataOperator().initRealJobBuildByRealJobConf(jobConfBean.getPluginConfId());
        jobBuild.setPluginBuildId(pluginBuildId);
        jobBuildDao.save(jobBuild);
        return jobBuild.getId();
    }

    @Override
    public void build(Long jobBuildId, Map<String, Object> execParam) {
        JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
        if (jobBuild == null) {
            throw new PipelineRuntimeException("TODO");
        }
        jobBuild.setStartTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuildDao.save(jobBuild);

        Long pipelineBuildId = jobBuild.getPipelineBuildId();
        PluginType pluginType = jobBuild.getPluginType();
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, execParam);
        pluginInfoService.getRealJobPlugin(pluginType).getExecutor().execute(jobBuildContext, jobBuild);
    }

    @Override
    public void buildTopJob(Long pipelineBuildId) {
        JobBuild jobBuild = jobBuildDao.getTopJob(pipelineBuildId);
        if (jobBuild == null) {
            throw new PipelineRuntimeException("TODO");
        }

        jobBuild.setStartTime(new Date());
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuildDao.save(jobBuild);

        // 手动则不执行
        if (TriggerMode.MANUAL.equals(jobBuild.getTriggerMode())) {
            return;
        }
        PluginType pluginType = jobBuild.getPluginType();
        // 自动执行的job无用户自定义参数
        JobBuildContext jobBuildContext = initJobBuildContext(pipelineBuildId, jobBuild, new HashMap<>());
        pluginInfoService.getRealJobPlugin(pluginType).getExecutor().execute(jobBuildContext, jobBuild);
    }

    private JobBuildContext initJobBuildContext(Long pipelineBuildId, JobBuild jobBuild, Map<String, Object> params) {
        JobBuildContext jobBuildContext = new JobBuildContext();
        PipelineBuild pipelineBuild = pipelineBuildDao.findById(pipelineBuildId);
        jobBuildContext.setPipelineBuild(pipelineBuild);
        jobBuildContext.setJobExecParam(params);
        // 处理jobBuild的参数
        Map<String, Object> originParams = new Gson().fromJson(jobBuild.getInParams(), Map.class);
        Map<String, Object> newParams = MapUtils.merge(originParams, params);
        jobBuild.setInParams(new Gson().toJson(newParams));
        jobBuildDao.save(jobBuild);
        return jobBuildContext;
    }

    @Override
    public void notifiedJobBuildFinished(JobBuild jobBuild) {
        // 保存job状态
        jobBuildDao.save(jobBuild);
        // 更新下游job状态
        Long jobBuildId = jobBuild.getId();
        Map<String, Object> params = jobBuild.getOutParameterMap();
        JobBuild lowStreamJobBuild = jobBuildDao.findById(jobBuildId);
        Map<String, Object> originParams = lowStreamJobBuild.getInParameterMap();
        Map<String, Object> newParams = MapUtils.merge(originParams, params);
        lowStreamJobBuild.setInParams(new Gson().toJson(newParams));
        jobBuildDao.save(lowStreamJobBuild);

        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)
                && TriggerMode.AUTO.equals(lowStreamJobBuild.getTriggerMode())) {
            // 如果下一个job是自动，则继续构建
            build(lowStreamJobBuild.getId(), new HashedMap());
        }
    }


}
