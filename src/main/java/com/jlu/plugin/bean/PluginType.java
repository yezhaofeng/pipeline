package com.jlu.plugin.bean;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public enum PluginType {
    COMPILE("编译构建"), RELEASE("发布"), JENKINS_JOB("Jenkins Job");

    private String pluginName;

    PluginType(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginName() {
        return pluginName;
    }
}
