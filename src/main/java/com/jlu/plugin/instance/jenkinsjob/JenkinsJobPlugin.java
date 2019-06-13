package com.jlu.plugin.instance.jenkinsjob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.plugin.AbstractPlugin;
import com.jlu.plugin.AbstractDataOperator;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.PluginConfig;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobConf;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
@Service
public class JenkinsJobPlugin extends AbstractPlugin<JenkinsJobConf, JenkinsJobBuild> {

    private final PluginType pluginType = PluginType.JENKINS_JOB;
    private final PluginConfig pluginConfig = new PluginConfig(pluginType, pluginType.getPluginName());

    @Autowired
    private JenkinsJobExecutor jenkinsJobExecutor;
    @Autowired
    private JenkinsJobDataOperator jenkinsJobDataOperator;

    @Override
    public AbstractExecutor getExecutor() {
        return jenkinsJobExecutor;
    }

    @Override
    public AbstractDataOperator<JenkinsJobConf, JenkinsJobBuild> getDataOperator() {
        return jenkinsJobDataOperator;
    }

    @Override
    public PluginConfig getConfig() {
        return pluginConfig;
    }
}
