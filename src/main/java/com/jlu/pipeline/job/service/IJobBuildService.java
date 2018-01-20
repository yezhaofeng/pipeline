package com.jlu.pipeline.job.service;

import java.util.List;
import java.util.Map;

import com.jlu.pipeline.job.bean.JobBuildBean;
import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by Administrator on 2018/1/18.
 */
public interface IJobBuildService {
    Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, String> params);

    // 当构建整条流水线的时候执行
    void buildTopJob(Long pipelineBuildId, TriggerMode triggerMode, String triggerUser);

    void build(Long jobBuildId, Map<String, String> execParam, TriggerMode triggerMode, String triggerUser);

    void notifiedJobBuildFinished(JobBuild jobBuild, Map<String, String> newOutParams);

    List<JobBuildBean> getJobBuildBeans(Long pipelineBuildId);

    void saveOrUpdate(JobBuild jobBuild);

    JobBuildBean getBuildInfo(Long jobBuildId);
}
