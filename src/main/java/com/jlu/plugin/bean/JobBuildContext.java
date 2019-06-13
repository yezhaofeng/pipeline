package com.jlu.plugin.bean;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by yezhaofeng on 2019/1/13.
 */
// job执行的上下文信息
public class JobBuildContext {
    PipelineBuild pipelineBuild;
    Map<String, Object> runtimePluginParam;
    Map<String, String> jobExecParam = new HashedMap();

    public Map<String, Object> getRuntimePluginParam() {
        return runtimePluginParam;
    }

    public void setRuntimePluginParam(Map<String, Object> runtimePluginParam) {
        this.runtimePluginParam = runtimePluginParam;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobBuildContext{");
        sb.append("pipelineBuild=").append(pipelineBuild);
        sb.append(", runtimePluginParam=").append(runtimePluginParam);
        sb.append(", jobExecParam=").append(jobExecParam);
        sb.append('}');
        return sb.toString();
    }
}
