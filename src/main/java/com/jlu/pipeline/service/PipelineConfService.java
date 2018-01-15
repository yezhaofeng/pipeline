package com.jlu.pipeline.service;

import com.jlu.pipeline.bean.PipelineConfBean;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface PipelineConfService {

    void processPipeline(PipelineConfBean pipelineConfBean, String userName);

    PipelineConfBean getPipelineConf(Long pipelineConfId);
}
