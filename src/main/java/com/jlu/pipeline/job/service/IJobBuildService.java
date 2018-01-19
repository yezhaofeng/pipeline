package com.jlu.pipeline.job.service;

import java.util.Map;

import com.jlu.pipeline.job.bean.JobConfBean;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by Administrator on 2018/1/18.
 */
public interface IJobBuildService {
    Long initBuild(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, Object> params);

    void buildTopJob(Long pipelineBuildId);

    void build(Long jobBuildId, Map<String, Object> execParam);

    void notifiedJobBuildFinished(JobBuild jobBuild);
}
