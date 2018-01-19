package com.jlu.jenkins.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.jlu.jenkins.model.JenkinsConf;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;

/**
 * Created by langshiquan on 17/12/23.
 */
public interface IJenkinsServerService {
    /**
     * @param serverUrl
     * @param username
     * @param password
     *
     * @return JenkinsServer
     */
    JenkinsServer getJenkinsServer(String serverUrl, String username, String password);

    /**
     * @param jenkinsConf
     *
     * @return JenkinsServer
     */
    JenkinsServer getJenkinsServer(JenkinsConf jenkinsConf);

    /**
     * @param jenkinsServer
     *
     * @return Job名字列表
     */
    Set<String> getJobs(JenkinsServer jenkinsServer) throws IOException;

    Boolean isExists(JenkinsServer jenkinsServer, String jobName) throws IOException;

    /**
     * job无参数
     *
     * @param jenkinsServer
     * @param jobName       jobName
     *
     * @return 构建号
     */
    @Deprecated
    Integer build(JenkinsServer jenkinsServer, String jobName) throws IOException;

    /**
     * job有参数
     *
     * @param jenkinsServer
     * @param jobName       jobName
     * @param params        参数
     *
     * @return 构建号
     */
    @Deprecated
    Integer buildWithParamters(JenkinsServer jenkinsServer, String jobName, Map<String, String> params)
            throws IOException;

    /**
     * 不区分job是否有参数，有参数则传递，无参数则不传递
     *
     * @param jenkinsServer
     * @param jobName       jobName
     * @param params        参数
     *
     * @return 构建号
     */
    Integer build(JenkinsServer jenkinsServer, String jobName, Map<String, String> params)
            throws IOException;

    /**
     * 获取上一次成功的构建的持续时间
     *
     * @param jenkinsServer
     * @param jobName
     *
     * @return
     *
     * @throws IOException
     */
    Long getLastSuccessfulBuildDuration(JenkinsServer jenkinsServer, String jobName) throws IOException;

    /**
     * @param jenkinsServer
     * @param jobName
     * @param buildNumber
     *
     * @return 构建
     *
     * @throws IOException
     */
    Build getJobBuild(JenkinsServer jenkinsServer, String jobName, Integer buildNumber) throws IOException;

}
