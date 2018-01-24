package com.jlu.pipeline.job.service;

import java.util.List;
import java.util.Map;

import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.JobRuntimeDTO;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.runtime.bean.RunTimeBean;

/**
 * Created by Administrator on 2018/1/18.
 */
public interface IJobBuildService {
    Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, String> params);

    // 当构建整条流水线的时候执行
    void buildTopJob(Long pipelineBuildId, TriggerMode triggerMode, String triggerUser);

    void build(Long jobBuildId, Map<String, String> execParam, TriggerMode triggerMode, String triggerUser);

    void build(Long jobBuildId, JobRuntimeDTO jobRuntimeDTO, TriggerMode triggerMode, String triggerUser);

    void notifiedJobBuildUpdated(JobBuild jobBuild, Map<String, String> newOutParams);

    List<JobBuildBean> getJobBuildBeans(Long pipelineBuildId);

    void saveOrUpdate(JobBuild jobBuild);

    JobBuildBean getBuildInfo(Long jobBuildId);

    List<RunTimeBean> getRuntimeRequire(Long jobBuildId);
}
