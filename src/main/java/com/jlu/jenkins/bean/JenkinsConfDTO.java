package com.jlu.jenkins.bean;

import com.jlu.jenkins.model.JenkinsConf;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsConfDTO {
    Long id;
    String serverUrl;
    String masterUser;
    String masterPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getMasterUser() {
        return masterUser;
    }

    public void setMasterUser(String masterUser) {
        this.masterUser = masterUser;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public JenkinsConf toJenkinsConf(String userName) {
        JenkinsConf jenkinsConf = new JenkinsConf();
        BeanUtils.copyProperties(this, jenkinsConf);
        jenkinsConf.setCreateTime(new Date());
        jenkinsConf.setCreateUser(userName);
        jenkinsConf.setLastModifiedUser(userName);
        return jenkinsConf;
    }
}
