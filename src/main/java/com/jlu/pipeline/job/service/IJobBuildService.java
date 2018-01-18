package com.jlu.pipeline.job.service;

import com.jlu.pipeline.job.bean.JobConfBean;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/18.
 */
public interface IJobBuildService {
    Long init(JobConfBean jobConfBean, Long pipelineBuildId, Long upStreamJobBuildId, Map<String, Object> params);

    void buildTopJob(Long pipelineBuildId);
}
