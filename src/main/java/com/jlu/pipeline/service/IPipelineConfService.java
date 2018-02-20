package com.jlu.pipeline.service;

import com.jlu.branch.bean.BranchType;
import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineConfService {

    PipelineConfBean getPipelineConfBean(Long pipelineConfId);

    PipelineConfBean getPipelineConfBean(String module, BranchType branchType);

    PipelineConf getPipelineConf(Long pipelineConfId);

    PipelineConf getPipelineConf(String module, String branchName);

    PipelineConf getPipelineConf(String module, BranchType branchType);

    void processPipelineWithTransaction(PipelineConfBean pipelineConfBean);

    void initDefaultConf(String module);
}
