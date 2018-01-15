package com.jlu.jenkins.bean;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsConfDTO {
    String serverUrl;
    String masterUser;
    String masterPassword;

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
}
