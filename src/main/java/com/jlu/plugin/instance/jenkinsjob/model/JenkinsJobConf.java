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
    private Long jenkinsConfId;
    private String jobName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJenkinsConfId() {
        return jenkinsConfId;
    }

    public void setJenkinsConfId(Long jenkinsConfId) {
        this.jenkinsConfId = jenkinsConfId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

}
