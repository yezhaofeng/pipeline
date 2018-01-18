package com.jlu.pipeline.service;

import com.jlu.github.model.GitHubCommit;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface PipelineBuildService {
    void build(Long pipelineConfId);

    void build(Long pipelineConfId, Long triggerId);

    void build(Long pipelineConfId, GitHubCommit gitHubCommit);

    Long initPipeline(PipelineConf pipelineConf);

    Long initPipeline(PipelineConf pipelineConf, GitHubCommit gitHubCommit);

}
