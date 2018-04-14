package com.jlu.jenkins.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.BeanUtils;

import com.jlu.jenkins.bean.JenkinsConfDTO;

/**
 * Created by langshiquan on 17/12/23.
 */
@Entity
public class JenkinsConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    private String serverName;
    private String serverUrl;
    private String masterUser;
    private String masterPassword;
    private Boolean deleteStatus = false;

    private String createUser;
    private Date createTime = new Date();
    private String lastModifiedUser;
    private Date lastModifiedTime = new Date();

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
        if(!serverUrl.endsWith("/")){
            serverUrl = serverUrl +"/";
        }
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

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public JenkinsConfDTO toJenkinsConfDTO() {
        JenkinsConfDTO jenkinsConfDTO = new JenkinsConfDTO();
        BeanUtils.copyProperties(jenkinsConfDTO, this);
        return jenkinsConfDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JenkinsConf that = (JenkinsConf) o;
        return Objects.equals(serverUrl, that.serverUrl) &&
                Objects.equals(masterUser, that.masterUser) &&
                Objects.equals(masterPassword, that.masterPassword);
    }

    // 重写equals的时候，必须重写hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(serverUrl, masterUser, masterPassword);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JenkinsConf{");
        sb.append("id=").append(id);
        sb.append(", serverUrl='").append(serverUrl).append('\'');
        sb.append(", masterUser='").append(masterUser).append('\'');
        sb.append(", masterPassword='").append(masterPassword).append('\'');
        sb.append(", deleteStatus=").append(deleteStatus);
        sb.append(", createUser='").append(createUser).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", lastModifiedUser='").append(lastModifiedUser).append('\'');
        sb.append(", lastModifiedTime=").append(lastModifiedTime);
        sb.append('}');
        return sb.toString();
    }
}
