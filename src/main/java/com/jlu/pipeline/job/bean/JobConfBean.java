package com.jlu.pipeline.job.bean;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jettison.json.JSONObject;

import com.jlu.common.deserializer.JSONObjectDeserializer;
import com.jlu.common.deserializer.JSONObjectSerializer;
import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobConfBean extends JobConf {
    private JSONObject pluginConf;
    private Map<String, String> parameterMap;

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
