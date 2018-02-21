package com.jlu.pipeline.service;

import java.util.List;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.aop.annotations.LogExecTime;
import com.jlu.github.model.GitHubCommit;
import com.jlu.pipeline.bean.PipelineBuildBean;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineBuildService {

    void build(Long pipelineConfId);

    void build(Long pipelineConfId, Long triggerId);

    void build(Long pipelineConfId, GitHubCommit gitHubCommit);

    void rebuild(Long triggerId);

    @LogExecTime
    Long initPipelineBuild(PipelineConf pipelineConf);

    @LogExecTime
    Long initPipelineBuild(PipelineConf pipelineConf, GitHubCommit gitHubCommit);

    PipelineBuild get(Long pipelineBuildId);

    List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId);

    List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, int offset, int limit);

    List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, String branchName);

    List<PipelineBuildBean> getPipelineBuildBean(Long pipelineConfId, String branchName, int offset, int limit);

    List<PipelineBuildBean> getPipelineBuildBean(String module, BranchType branchType, int offset, int limit);

    List<PipelineBuildBean> getPipelineBuildBean(String module, BranchType branchType, String branchName, int offset, int limit);
}
