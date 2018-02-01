package com.jlu.github.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jlu.common.utils.bean.AbstractPropertyGetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepoBean {

    public static final AbstractPropertyGetter<String, GithubRepoBean> NAME_GETTER =
            new AbstractPropertyGetter<String, GithubRepoBean>() {
                @Override
                public String get(GithubRepoBean githubRepoBean) {
                    return null == githubRepoBean ? null : githubRepoBean.getName();
                }
            };
    private String name;

    private String git_url;

    private String ssh_url;

    private String hooks_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGit_url() {
        return git_url;
    }

    public void setGit_url(String git_url) {
        this.git_url = git_url;
    }

    public String getSsh_url() {
        return ssh_url;
    }

    public void setSsh_url(String ssh_url) {
        this.ssh_url = ssh_url;
    }

    public String getHooks_url() {
        return hooks_url;
    }

    public void setHooks_url(String hooks_url) {
        this.hooks_url = hooks_url;
    }
}
