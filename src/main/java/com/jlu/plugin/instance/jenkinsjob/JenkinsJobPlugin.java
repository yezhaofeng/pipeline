package com.jlu.plugin.instance.jenkinsjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.IDataOperator;
import com.jlu.plugin.IExecutor;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobConf;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobPlugin extends AbstractPlugin<JenkinsJobBuild,JenkinsJobConf> {

    private final PluginType pluginType = PluginType.JENKINS_JOB;
    private final PluginConfig pluginConfig = new PluginConfig(pluginType, pluginType.getPluginName());

    @Autowired
    private JenkinsJobExecutor jenkinsJobExecutor;
    @Autowired
    private JenkinsJobDataOperator jenkinsJobDataOperator;

    @Override
    public IExecutor getExecutor() {
        return jenkinsJobExecutor;
    }

    @Override
    public IDataOperator<JenkinsJobBuild, JenkinsJobConf> getDataOperator() {
        return jenkinsJobDataOperator;
    }

    @Override
    public PluginConfig getConfig() {
        return pluginConfig;
    }
}
