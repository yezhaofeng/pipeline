package com.jlu.pipeline.job.bean;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/25.
 */
public class JobRuntimeDTO {
    Map<String, String> execParam;
    Map<String, Object> runtimePluginParam;

    public Map<String, String> getExecParam() {
        return execParam;
    }

    public void setExecParam(Map<String, String> execParam) {
        this.execParam = execParam;
    }

    public Map<String, Object> getRuntimePluginParam() {
        return runtimePluginParam;
    }

    public void setRuntimePluginParam(Map<String, Object> runtimePluginParam) {
        this.runtimePluginParam = runtimePluginParam;
    }
}
