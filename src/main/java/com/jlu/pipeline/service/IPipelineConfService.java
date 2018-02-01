package com.jlu.pipeline.service;

import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineConfService {

    void processPipelineWithTransaction(PipelineConfBean pipelineConfBean, String userName);

    PipelineConfBean getPipelineConfBean(Long pipelineConfId);

    PipelineConf getPipelineConf(Long pipelineConfId);

    PipelineConf getPipelineConf(String owner, String module, String branchName);

    void initDefaultConf(String owner, String module);
}
