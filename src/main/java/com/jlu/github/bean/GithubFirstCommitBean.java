package com.jlu.github.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jlu.github.model.GitHubCommit;

/**
 * Created by Administrator on 2018/1/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubFirstCommitBean {
    @JsonProperty("sha")
    private String sha;

    private Commit commit;
    @JsonProperty("html_url")
    private String html_url;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public class Commit {
        private GitHubCommitBean.Committer author;
        private GitHubCommitBean.Committer committer;
        private String message;

        public GitHubCommitBean.Committer getAuthor() {
            return author;
        }

        public void setAuthor(GitHubCommitBean.Committer author) {
            this.author = author;
        }

        public GitHubCommitBean.Committer getCommitter() {
            return committer;
        }

        public void setCommitter(GitHubCommitBean.Committer committer) {
            this.committer = committer;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public GitHubCommit toGithubCommit() {
        GitHubCommit gitHubCommit = new GitHubCommit();
        gitHubCommit.setCommitUrl(this.getHtml_url());
        gitHubCommit.setCommitter(this.getCommit().getCommitter().getName());
        gitHubCommit.setCommitterEmail(this.getCommit().getCommitter().getEmail());
        gitHubCommit.setCommitId(this.getSha());
        return gitHubCommit;
    }

}
