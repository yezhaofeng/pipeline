package com.jlu.plugin.bean;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/13.
 */
// job执行的上下文信息
public class JobBuildContext {
    PipelineBuild pipelineBuild;

    Map<String, String> jobExecParam = new HashedMap();

    public PipelineBuild getPipelineBuild() {
        return pipelineBuild;
    }

    public void setPipelineBuild(PipelineBuild pipelineBuild) {
        this.pipelineBuild = pipelineBuild;
    }

    public Map<String, String> getJobExecParam() {
        return jobExecParam;
    }

    public void setJobExecParam(Map<String, String> jobExecParam) {
        this.jobExecParam = jobExecParam;
    }
}
