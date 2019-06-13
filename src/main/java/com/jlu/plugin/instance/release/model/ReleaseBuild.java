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
import org.apache.commons.lang.StringUtils;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Entity
public class ReleaseBuild {

    /**
     * --no-passive-ftp 不使用被动模式
     * -r, --recursive 递归下载
     * -nH, --no-host-directories 不创建含有远程主机名称的目录。
     * -l, --level=数字 最大递归深度(inf 或 0 表示无限)。
     * --cut-dirs=数目 忽略远程目录中指定数目的目录层,在此例中4层是指从release到版本号，output是第五层，只下载output文件夹
     * -p,--preserve-permissions 提取有关文件权限的信息（超级用户默认选项）,即wget下来的文件的权限不变
     */
    private static final String WGET_COMMAND_PREFIX = "wget --no-passive-ftp -r -nH --level=0 --cut-dirs=4 ";
    private static final String WGET_COMMAND_SUFFIX =
            " --user ftptest --password ftptest --preserve-permissions";

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

    private Integer buildNumber;

    @RuntimeRequire(defaultValueClass = VersionValueGenerator.class,
            checkRegex = ReleaseServiceImpl.VERSION_REGEX, description = "版本号")
    private String version;

    @Enumerated(EnumType.STRING)
    private PipelineJobStatus status;

    @RuntimeRequire(description = "发布备注")
    private String remark;

    private String message;

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
    }

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
        if(StringUtils.isBlank(releasePath)){
            return null;
        }
        return WGET_COMMAND_PREFIX + releasePath + WGET_COMMAND_SUFFIX;
    }

}
