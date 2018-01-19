package com.jlu.pipeline.service;

import com.jlu.github.model.GitHubCommit;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineBuildService {
    void build(Long pipelineConfId);

    void build(Long pipelineConfId, Long triggerId);

    void build(Long pipelineConfId, GitHubCommit gitHubCommit);

    Long initPipelineBuild(PipelineConf pipelineConf);

    Long initPipelineBuild(PipelineConf pipelineConf, GitHubCommit gitHubCommit);

}
