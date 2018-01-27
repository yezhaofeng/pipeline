package com.jlu.pipeline.model;

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
 * Created by langshiquan on 18/1/10.
 */
@Entity
public class PipelineConf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected String remark;
    protected Boolean isAuto;
    protected String module;
    protected String createUser;
    protected Date createTime;
    protected Date lastModifiedTime;
    protected String lastModifiedUser;
    private String owner;
    @Enumerated(EnumType.STRING)
    protected BranchType branchType;
    @JsonIgnore
    private Boolean deleteStats = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getAuto() {
        return isAuto;
    }

    public void setAuto(Boolean auto) {
        isAuto = auto;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
    }

    public Boolean getDeleteStats() {
        return deleteStats;
    }

    public void setDeleteStats(Boolean deleteStats) {
        this.deleteStats = deleteStats;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PipelineConf{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", isAuto=").append(isAuto);
        sb.append(", module='").append(module).append('\'');
        sb.append(", createUser='").append(createUser).append('\'');
        sb.append(", createTime=").append(createTime);
        sb.append(", lastModifiedTime=").append(lastModifiedTime);
        sb.append(", lastModifiedUser='").append(lastModifiedUser).append('\'');
        sb.append(", owner='").append(owner).append('\'');
        sb.append(", branchType=").append(branchType);
        sb.append(", deleteStats=").append(deleteStats);
        sb.append('}');
        return sb.toString();
    }
}
