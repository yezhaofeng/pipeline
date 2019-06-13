package com.jlu.user.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.user.bean.Role;

/**
 * Created by yezhaofeng on 2019/3/10.
 *
 *  用户信息实体类
 */
@Entity
public class GithubUser implements Serializable{

    public static final String CURRENT_USER_NAME="currentUser";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String email;

    private String avatarUrl;

    private String pipelineToken;
    @JsonIgnore
    private String gitHubToken;
    @JsonIgnore
    private Date createTime;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public GithubUser() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getGitHubToken() {
        return gitHubToken;
    }

    public void setGitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPipelineToken() {
        return pipelineToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPipelineToken(String pipelineToken) {
        this.pipelineToken = pipelineToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GithubUser{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", gitHubToken='").append(gitHubToken).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }

    public String getGithubHome() {
        return PipelineConfigReader.getConfigValueByKey("github.user.home");
    }
}
