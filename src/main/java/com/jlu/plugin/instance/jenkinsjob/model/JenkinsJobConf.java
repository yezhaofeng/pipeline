package com.jlu.plugin.instance.jenkinsjob.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by langshiquan on 18/1/10.
 */
@Entity
public class JenkinsJobConf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jenkinsServerId;
    private String jobName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJenkinsServerId() {
        return jenkinsServerId;
    }

    public void setJenkinsServerId(Long jenkinsServerId) {
        this.jenkinsServerId = jenkinsServerId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JenkinsJobConf{");
        sb.append("id=").append(id);
        sb.append(", jenkinsServerId=").append(jenkinsServerId);
        sb.append(", jobName='").append(jobName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
