package com.jlu.common.service;

import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.jenkins.timer.service.IScheduledService;
import com.jlu.plugin.service.IPluginInfoService;
import com.jlu.plugin.thread.PluginThreadService;
import com.jlu.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/19.
 */
@Component
public class ServiceBeanFactory {
    private static IJenkinsBuildService jenkinsBuildService;
    private static IJenkinsServerService jenkinsServerService;
    private static IScheduledService scheduledService;
    private static IUserService userService;
    private static IPluginInfoService pluginInfoService;
    private static PluginThreadService pluginThreadService;

    @Autowired
    public void setPluginThreadService(PluginThreadService pluginThreadService) {
        ServiceBeanFactory.pluginThreadService = pluginThreadService;
    }

    @Autowired
    public void setJenkinsBuildService(IJenkinsBuildService jenkinsBuildService) {
        ServiceBeanFactory.jenkinsBuildService = jenkinsBuildService;
    }

    @Autowired
    public void setJenkinsServerService(IJenkinsServerService jenkinsServerService) {
        ServiceBeanFactory.jenkinsServerService = jenkinsServerService;
    }

    @Autowired
    public void setScheduledService(IScheduledService scheduledService) {
        ServiceBeanFactory.scheduledService = scheduledService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        ServiceBeanFactory.userService = userService;
    }

    @Autowired
    public void setPluginInfoService(IPluginInfoService pluginInfoService) {
        ServiceBeanFactory.pluginInfoService = pluginInfoService;
    }

    public static PluginThreadService getPluginThreadService() {
        return pluginThreadService;
    }

    public static IPluginInfoService getPluginInfoService() {
        return pluginInfoService;
    }

    public static IJenkinsBuildService getJenkinsBuildService() {
        return jenkinsBuildService;
    }

    public static IJenkinsServerService getJenkinsServerService() {
        return jenkinsServerService;
    }

    public static IScheduledService getScheduledService() {
        return scheduledService;
    }

    public static IUserService getUserService() {
        return userService;
    }
}
