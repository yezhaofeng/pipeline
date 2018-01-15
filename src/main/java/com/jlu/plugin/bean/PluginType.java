package com.jlu.plugin.bean;

/**
 * Created by langshiquan on 18/1/10.
 */
public enum PluginType {
    COMPILE("编译构建"), RELEASE("发版"), JENKINS_JOB("Jenkins Job");

    private String pluginName;

    PluginType(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }
}
