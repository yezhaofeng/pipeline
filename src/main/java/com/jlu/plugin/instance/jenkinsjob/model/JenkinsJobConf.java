package com.jlu.plugin.instance.jenkinsjob.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by langshiquan on 18/1/10.
 */
@Entity
public class JenkinsJobConf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @NotNull
    private Long jenkinsServerId;
    //    @NotBlank
    private String jobFullName;
    //    @NotBlank
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

    public String getJobFullName() {
        return jobFullName;
    }

    public void setJobFullName(String jobFullName) {
        this.jobFullName = jobFullName;
    }

    @Override
    public String toString() {
        return "JenkinsJobConf{" +
                "id=" + id +
                ", jenkinsServerId=" + jenkinsServerId +
                ", jobFullName='" + jobFullName + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }

    public boolean isValid() {
        return !(jenkinsServerId == null || jobName == null || jobFullName == null);
    }
}
