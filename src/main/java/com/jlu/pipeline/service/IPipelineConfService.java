package com.jlu.pipeline.service;

import com.jlu.branch.bean.BranchType;
import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineConfService {

    void processPipelineWithTransaction(PipelineConfBean pipelineConfBean);

    PipelineConfBean getPipelineConfBean(Long pipelineConfId);

    PipelineConf getPipelineConf(Long pipelineConfId);

    PipelineConf getPipelineConf(String module, String branchName);

    void initDefaultConf(String module);

    PipelineConfBean getPipelineConfBean(String module, BranchType branchType);

    void processPipelineWithTransaction(PipelineConfBean pipelineConfBean, String module, BranchType branchType);
}
