package com.jlu.jenkins.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.jlu.jenkins.model.JenkinsConf;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;

/**
 * Created by yezhaofeng on 2019/12/23.
 */
public interface IJenkinsServerService {

    JenkinsServer getJenkinsServer(String serverUrl, String username, String password);

    JenkinsServer getJenkinsServer(JenkinsConf jenkinsConf);

    Set<String> getJobs(JenkinsServer jenkinsServer) throws IOException;

    Boolean isExists(JenkinsServer jenkinsServer, String jobName) throws IOException;

    /**
     * 不区分job是否有参数，有参数则传递，无参数则不传递
     *
     * @param jenkinsServer
     * @param jobName       jobName
     * @param params        参数
     * @return 构建号
     */
    Integer build(JenkinsServer jenkinsServer, String jobName, Map<String, String> params)
            throws IOException;

    void cancel(JenkinsServer jenkinsServer, String jobName,Integer buildNumber) throws IOException;

    Long getLastSuccessfulBuildDuration(JenkinsServer jenkinsServer, String jobName) throws IOException;

    Build getJobBuild(JenkinsServer jenkinsServer, String jobName, Integer buildNumber) throws IOException;

    /**
     * 构建无参数job
     *
     * @param jenkinsServer
     * @param jobName       jobName
     * @return 构建号
     */
    @Deprecated
    Integer build(JenkinsServer jenkinsServer, String jobName) throws IOException;

    /**
     * 构建有参数job
     *
     * @param jenkinsServer
     * @param jobName       jobName
     * @param params        参数
     * @return 构建号
     */
    @Deprecated
    Integer buildWithParameters(JenkinsServer jenkinsServer, String jobName, Map<String, String> params)
            throws IOException;

}
