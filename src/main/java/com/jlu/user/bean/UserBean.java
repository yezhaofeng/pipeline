package com.jlu.user.bean;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import com.jlu.user.model.GithubUser;

/**
 * Created by niuwanpeng on 17/3/29.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {

    private String token;
    @NotNull
    private String username;

    @NotNull
    private String gitHubToken;

    @NotNull
    private String email;

    // 头像地址
    private String avatarUrl;

    private List<String> syncRepos;

    private String registerToken;

    public UserBean() {
    }

    public String getRegisterToken() {
        return registerToken;
    }

    public void setRegisterToken(String registerToken) {
        this.registerToken = registerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGitHubToken() {
        return gitHubToken;
    }

    public void setGitHubToken(String gitHubToken) {
        this.gitHubToken = gitHubToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<String> getSyncRepos() {
        return syncRepos;
    }

    public void setSyncRepos(List<String> syncRepos) {
        this.syncRepos = syncRepos;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserBean{");
        sb.append("username='").append(username).append('\'');
        sb.append(", gitHubToken='").append(gitHubToken).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", syncRepos=").append(syncRepos);
        sb.append('}');
        return sb.toString();
    }

    public GithubUser toUser() {
        GithubUser githubUser = new GithubUser();
        BeanUtils.copyProperties(this, githubUser);
        githubUser.setCreateTime(new Date());
        return githubUser;
    }
}
