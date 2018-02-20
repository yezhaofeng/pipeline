package com.jlu.github.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.utils.PipelineConfigReader;

/**
 * Created by langshiquan on 17/4/25.
 * <p>
 * 代码提交信息
 */
@Entity
public class GitHubCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String module;

    private String branch;

    @Enumerated(EnumType.STRING)
    private BranchType branchType;

    private String committer;

    private String committerEmail;

    private String message;

    private String commitTime;

    private String addedFiles;

    private String removedFiles;

    private String modifiedFiles;

    private String commitId;

    private String commitUrl;

    private String owner;

    public GitHubCommit(String committer, String committerEmail, String message, String commitTime,
                        String addedFiles, String removedFiles, String modifiedFiles, String commitId,
                        String commitUrl) {
        this.committer = committer;
        this.committerEmail = committerEmail;
        this.message = message;
        this.commitTime = commitTime;
        this.addedFiles = addedFiles;
        this.removedFiles = removedFiles;
        this.modifiedFiles = modifiedFiles;
        this.commitId = commitId;
        this.commitUrl = commitUrl;
    }

    public GitHubCommit() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }


    public String getCommitter() {
        return committer;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }

    public String getCommits() {
        return message;
    }

    public void setCommits(String message) {
        this.message = message;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getAddedFiles() {
        return addedFiles;
    }

    public void setAddedFiles(String addedFiles) {
        this.addedFiles = addedFiles;
    }

    public String getRemovedFiles() {
        return removedFiles;
    }

    public void setRemovedFiles(String removedFiles) {
        this.removedFiles = removedFiles;
    }

    public String getModifiedFiles() {
        return modifiedFiles;
    }

    public void setModifiedFiles(String modifiedFiles) {
        this.modifiedFiles = modifiedFiles;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }

    public String getTreeUrl() {
        return PipelineConfigReader.getConfigValueByKey("github.user.home") + owner + "/" + module + "/tree/" + commitId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GitHubCommit{");
        sb.append("id=").append(id);
        sb.append(", module='").append(module).append('\'');
        sb.append(", branch='").append(branch).append('\'');
        sb.append(", branchType=").append(branchType);
        sb.append(", committer='").append(committer).append('\'');
        sb.append(", committerEmail='").append(committerEmail).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", commitTime='").append(commitTime).append('\'');
        sb.append(", addedFiles='").append(addedFiles).append('\'');
        sb.append(", removedFiles='").append(removedFiles).append('\'');
        sb.append(", modifiedFiles='").append(modifiedFiles).append('\'');
        sb.append(", commitId='").append(commitId).append('\'');
        sb.append(", commitUrl='").append(commitUrl).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
