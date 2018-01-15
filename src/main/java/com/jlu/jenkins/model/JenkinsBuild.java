package com.jlu.jenkins.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * Created by langshiquan on 18/1/13.
 */
@Entity
public class JenkinsBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jenkinsConfId;
    private Long pipelineJobBuildId;
    private String jobName;
    private Integer buildNumber;

    @Enumerated(EnumType.STRING)
    private BuildResult buildResult;
    private String triggerUser;
    private Date startTime;
    private Date endTime;
    private Date realEndTime;

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

    public Long getPipelineJobBuildId() {
        return pipelineJobBuildId;
    }

    public void setPipelineJobBuildId(Long pipelineJobBuildId) {
        this.pipelineJobBuildId = pipelineJobBuildId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

    public BuildResult getBuildResult() {
        return buildResult;
    }

    public void setBuildResult(BuildResult buildResult) {
        this.buildResult = buildResult;
    }

    public String getTriggerUser() {
        return triggerUser;
    }

    public void setTriggerUser(String triggerUser) {
        this.triggerUser = triggerUser;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(Date realEndTime) {
        this.realEndTime = realEndTime;
    }
}
