package com.jlu.pipeline.job.service.impl;

import com.alibaba.fastjson.JSON;
import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.service.IPluginInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/18.
 */
@Service
public class JobBuildServiceImpl implements IJobBuildService {

    @Autowired
    private IPluginInfoService pluginInfoService;

    @Autowired
    private IJobBuildDao jobBuildDao;

    @Override
    public Long init(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, Object> params) {
        JobBuild jobBuild = new JobBuild();
        jobBuild.setJobConfId(jobConfBean.getId());
        jobBuild.setUpStreamJobBuildId(upStreamJobBuildId);
        jobBuild.setPipelineBuildId(pipelineBuildId);
        jobBuild.setJobStatus(PipelineJobStatus.INIT);
        jobBuild.setName(jobConfBean.getName());
        Map<String, Object> confParam = jobConfBean.getParameterMap();
        Map<String, Object> mergedParam = mergeParams(confParam, params);
        jobBuild.setInParams(JSON.toJSONString(mergedParam));
        PluginType pluginType = jobConfBean.getPluginType();
        jobBuild.setPluginType(pluginType);
        Long pluginBuildId = pluginInfoService.getRealJobPlugin(pluginType).getDataOperator().initRealJobBuildByRealJobConf(jobConfBean.getPluginConfId());
        jobBuild.setPluginBuildId(pluginBuildId);
        jobBuildDao.save(jobBuild);
        return jobBuild.getId();
    }

    // 系统参数优于用户自定义参数
    private Map<String, Object> mergeParams(Map<String, Object> confParam, Map<String, Object> params) {
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            params.put(key, confParam.get(key));
        }
        return confParam;
    }

    @Override
    public void buildTopJob(Long pipelineBuildId) {
        JobBuild jobBuild = jobBuildDao.get(pipelineBuildId, 0L);
        if (jobBuild == null) {
            throw new PipelineRuntimeException("TODO");
        }
        PluginType pluginType = jobBuild.getPluginType();
        JobBuildContext jobBuildContext = new JobBuildContext();
        pluginInfoService.getRealJobPlugin(pluginType).getExecutor().execute(jobBuildContext, jobBuild);
    }
}
