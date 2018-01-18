package com.jlu.pipeline.service;

import com.jlu.pipeline.bean.PipelineConfBean;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface PipelineConfService {

    void processPipeline(PipelineConfBean pipelineConfBean, String userName);

    PipelineConfBean getPipelineConfBean(Long pipelineConfId);

    PipelineConf getPipelineConf(Long pipelineConfId);

    PipelineConf getPipelineConfBean(String name, String module, String branchName);
}
