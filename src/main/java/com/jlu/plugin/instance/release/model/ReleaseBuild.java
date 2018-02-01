package com.jlu.plugin.instance.release.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.jlu.common.utils.bean.AbstractPropertyGetter;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.plugin.instance.release.VersionValueGenerator;
import com.jlu.plugin.instance.release.service.impl.ReleaseServiceImpl;
import com.jlu.plugin.runtime.RuntimeRequire;

/**
 * Created by langshiquan on 18/1/20.
 */
@Entity
public class ReleaseBuild {

    private static final String WGET_COMMAND_PREFIX = "wget -r -nH --level=0 --cut-dirs=6 ";
    private static final String WGET_COMMAND_SUFFIX =
            "/output --user release --password release@123 --preserve-permissions";

    public static final AbstractPropertyGetter<String, ReleaseBuild> VERSION_GETTER =
            new AbstractPropertyGetter<String, ReleaseBuild>() {
                @Override
                public String get(ReleaseBuild releaseBuild) {
                    return null == releaseBuild ? null : releaseBuild.getVersion();
                }
            };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String module;

    private String branch;

    private String pipelineName;

    private String commitId;

    private String triggerUser;

    private String logUrl;

    private String releasePath;

    @RuntimeRequire(defaultValueClass = VersionValueGenerator.class,
            checkRegex = ReleaseServiceImpl.VERSION_REGEX, description = "版本号")
    private String version;

    @Enumerated(EnumType.STRING)
    private PipelineJobStatus status;

    @RuntimeRequire(description = "发布备注")
    private String remark;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getReleasePath() {
        return releasePath;
    }

    public void setReleasePath(String releasePath) {
        this.releasePath = releasePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getTriggerUser() {
        return triggerUser;
    }

    public void setTriggerUser(String triggerUser) {
        this.triggerUser = triggerUser;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public PipelineJobStatus getStatus() {
        return status;
    }

    public void setStatus(PipelineJobStatus status) {
        this.status = status;
    }

    public String getReleaseProductWgetCommand() {
        return WGET_COMMAND_PREFIX + releasePath + WGET_COMMAND_SUFFIX;
    }

}
