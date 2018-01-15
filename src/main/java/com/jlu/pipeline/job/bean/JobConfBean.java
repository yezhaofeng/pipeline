package com.jlu.pipeline.job.bean;

import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
public class JobConfBean extends JobConf {
    private JSONObject pluginConf;
    private Map<String, String> parameterMap;

    public JSONObject getPluginConf() {
        return pluginConf;
    }

    public void setPluginConf(JSONObject pluginConf) {
        this.pluginConf = pluginConf;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
