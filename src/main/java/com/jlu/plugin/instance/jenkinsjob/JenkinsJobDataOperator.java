package com.jlu.plugin.instance.jenkinsjob;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.jlu.plugin.IDataOperator;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobDataOperator extends IDataOperator<JenkinsJobBuild, JenkinsJobConf> {
    @Override
    public Long saveJob(JSONObject json) {
        try {
            System.out.println(json.get("jobName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10000L;
    }

    @Override
    public JenkinsJobBuild getJob(Long id) {
        return null;
    }

    @Override
    public JenkinsJobConf getBuild(Long id) {
        return null;
    }
}
