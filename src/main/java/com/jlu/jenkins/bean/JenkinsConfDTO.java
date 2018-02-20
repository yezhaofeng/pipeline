package com.jlu.jenkins.bean;

import java.util.Date;

import com.jlu.common.interceptor.UserLoginHelper;
import org.springframework.beans.BeanUtils;

import com.jlu.jenkins.model.JenkinsConf;

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

    public JenkinsConf toJenkinsConf() {
        JenkinsConf jenkinsConf = new JenkinsConf();
        BeanUtils.copyProperties(this, jenkinsConf);
        jenkinsConf.setCreateTime(new Date());
        jenkinsConf.setCreateUser(UserLoginHelper.getLoginUserName());
        jenkinsConf.setLastModifiedUser(UserLoginHelper.getLoginUserName());
        return jenkinsConf;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JenkinsConfDTO{");
        sb.append("id=").append(id);
        sb.append(", serverUrl='").append(serverUrl).append('\'');
        sb.append(", masterUser='").append(masterUser).append('\'');
        sb.append(", masterPassword='").append(masterPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
