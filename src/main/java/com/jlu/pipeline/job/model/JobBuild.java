package com.jlu.pipeline.job.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.plugin.bean.PluginType;

/**
 * Created by langshiquan on 18/1/10.
 */
@Entity
public class JobBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Long jobConfId;
    protected Long upStreamJobBuildId;
    protected Long pipelineBuildId;
    protected String name;
    @Enumerated(EnumType.STRING)
    protected PluginType pluginType;

    protected Long pluginBuildId;
    @Enumerated(EnumType.STRING)
    protected PipelineJobStatus jobStatus;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    protected String inParams;

    @JsonIgnore
    @Lob
    @Column(columnDefinition = "TEXT")
    protected String outParams;
    protected String triggerUser;

    @Enumerated(EnumType.STRING)
    protected TriggerMode triggerMode;
    // 任务发起时间
    protected Date triggerTime;
    // 插件开始执行的时间
    protected Date startTime;
    // 结束时间
    protected Date endTime;
    protected String message;

    public Long getUpStreamJobBuildId() {
        return upStreamJobBuildId;
    }

    public void setUpStreamJobBuildId(Long upStreamJobBuildId) {
        this.upStreamJobBuildId = upStreamJobBuildId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobConfId() {
        return jobConfId;
    }

    public void setJobConfId(Long jobConfId) {
        this.jobConfId = jobConfId;
    }

    public Long getPipelineBuildId() {
        return pipelineBuildId;
    }

    public void setPipelineBuildId(Long pipelineBuildId) {
        this.pipelineBuildId = pipelineBuildId;
    }

    public Long getPluginBuildId() {
        return pluginBuildId;
    }

    public void setPluginBuildId(Long pluginBuildId) {
        this.pluginBuildId = pluginBuildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public void setPluginType(PluginType pluginType) {
        this.pluginType = pluginType;
    }

    public PipelineJobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(PipelineJobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getInParams() {
        return inParams;
    }

    public void setInParams(String inParams) {
        this.inParams = inParams;
    }

    public String getOutParams() {
        return outParams;
    }

    public void setOutParams(String outParams) {
        this.outParams = outParams;
    }

    public String getTriggerUser() {
        return triggerUser;
    }

    public void setTriggerUser(String triggerUser) {
        this.triggerUser = triggerUser;
    }

    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(TriggerMode triggerMode) {
        this.triggerMode = triggerMode;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getInParameterMap() {
        if (StringUtils.isBlank(getInParams())) {
            return new HashMap<>();
        }
        return new HashMap<>((Map) JSON.parseObject(getInParams()));
    }

    public Map<String, String> getOutParameterMap() {
        if (StringUtils.isBlank(getOutParams())) {
            return new HashMap<>();
        }
        return new HashMap<>((Map) JSON.parseObject(getOutParams()));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JobBuild{");
        sb.append("id=").append(id);
        sb.append(", jobConfId=").append(jobConfId);
        sb.append(", upStreamJobBuildId=").append(upStreamJobBuildId);
        sb.append(", pipelineBuildId=").append(pipelineBuildId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", pluginType=").append(pluginType);
        sb.append(", pluginBuildId=").append(pluginBuildId);
        sb.append(", jobStatus=").append(jobStatus);
        sb.append(", inParams='").append(inParams).append('\'');
        sb.append(", outParams='").append(outParams).append('\'');
        sb.append(", triggerUser='").append(triggerUser).append('\'');
        sb.append(", triggerMode=").append(triggerMode);
        sb.append(", triggerTime=").append(triggerTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
