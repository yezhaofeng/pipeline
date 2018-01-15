package com.jlu.pipeline.service;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface PipelineBuildService {
    void build(Long pipelineConfId);

    void build(Long pipelineConfId, Long triggerId);
}
