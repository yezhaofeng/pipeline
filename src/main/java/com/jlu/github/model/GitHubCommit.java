package com.jlu.github.model;

import com.jlu.branch.bean.BranchType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by niuwanpeng on 17/4/25.
 *
 * 代码提交信息
 */
@Entity
public class GitHubCommit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String module;

    private String branch;

    private BranchType branchType;

    private String committer;

    private String committerEmail;

    private String commits;

    private String commitTime;

    private String addedFiles;

    private String removedFiles;

    private String  modifiedFiles;

    private String commitId;

    private String commitUrl;

    private String owner;

    public GitHubCommit( String committer, String committerEmail, String commits, String commitTime,
                        String addedFiles, String removedFiles, String modifiedFiles, String commitId,
                        String commitUrl) {
        this.committer = committer;
        this.committerEmail = committerEmail;
        this.commits = commits;
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
        return commits;
    }

    public void setCommits(String commits) {
        this.commits = commits;
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

}
