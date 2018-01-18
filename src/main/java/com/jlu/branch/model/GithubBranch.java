package com.jlu.branch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.jlu.branch.bean.BranchType;

/**
 * Created by niuwanpeng on 17/3/10.
 *
 * 分支实体类, 与用户名关联
 */
@Entity
public class GithubBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int moduleId;

    private String branchName;

    private BranchType branchType;

    private String createTime;

    private String version;

    private String remarks;

    public GithubBranch(int moduleId, String branchName, BranchType branchType, String version, String createTime) {
        this.moduleId = moduleId;
        this.branchName = branchName;
        this.branchType = branchType;
        this.version = version;
        this.createTime = createTime;
    }

    public GithubBranch() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
