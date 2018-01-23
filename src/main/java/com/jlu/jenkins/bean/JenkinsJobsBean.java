package com.jlu.jenkins.bean;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/1/23.
 */
public class JenkinsJobsBean {
    private String serverUrl;
    private Long jenkinsServerId;
    private Set<String> jobs;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Long getJenkinsServerId() {
        return jenkinsServerId;
    }

    public void setJenkinsServerId(Long jenkinsServerId) {
        this.jenkinsServerId = jenkinsServerId;
    }

    public Set<String> getJobs() {
        return jobs;
    }

    public void setJobs(Set<String> jobs) {
        this.jobs = jobs;
    }
}
