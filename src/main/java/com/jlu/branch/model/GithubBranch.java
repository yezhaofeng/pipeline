package com.jlu.branch.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Deprecated
    @JsonIgnore
    private int moduleId;

    private String module;

    private String branchName;

    @Enumerated(EnumType.STRING)
    private BranchType branchType;

    private Date createTime;

    private String remarks;

    public GithubBranch(String module, String branchName, BranchType branchType) {
        this.module = module;
        this.branchName = branchName;
        this.branchType = branchType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public GithubBranch() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Deprecated
    public int getModuleId() {
        return moduleId;
    }

    @Deprecated
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
