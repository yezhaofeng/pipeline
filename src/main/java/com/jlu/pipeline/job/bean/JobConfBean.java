package com.jlu.pipeline.job.bean;

import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jlu.common.deserializer.JSONObjectDeserializer;
import com.jlu.common.deserializer.JSONObjectSerializer;
import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobConfBean extends JobConf {
    private JSONObject pluginConf;

    private Map<String, String> parameterMap;

    // jackson2.x 无JSONObject对象
    // 此处是做一个适配
    @JsonSerialize(using = JSONObjectSerializer.class)
    public JSONObject getPluginConf() {
        return pluginConf;
    }

    @JsonDeserialize(using = JSONObjectDeserializer.class)
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
