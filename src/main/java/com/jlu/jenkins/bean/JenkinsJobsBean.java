package com.jlu.jenkins.bean;

import java.util.Set;

/**
 * Created by Administrator on 2019/1/23.
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JenkinsJobsBean{");
        sb.append("serverUrl='").append(serverUrl).append('\'');
        sb.append(", jenkinsServerId=").append(jenkinsServerId);
        sb.append(", jobs=").append(jobs);
        sb.append('}');
        return sb.toString();
    }
}
