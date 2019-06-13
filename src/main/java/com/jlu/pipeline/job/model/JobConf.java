package com.jlu.pipeline.job.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jlu.pipeline.job.bean.TriggerMode;
import com.jlu.plugin.bean.PluginType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
@Entity
public class JobConf {
    public static final String DEFAULT_PARAMS = "{}";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected Long upStreamJobConfId;
    protected Long pipelineConfId;

    protected String name;
    @Enumerated(EnumType.STRING)
    protected PluginType pluginType;
    @Enumerated(EnumType.STRING)
    protected TriggerMode triggerMode;
    protected Long pluginConfId;

    /**
     * {
     * "key":"defaultValue"
     * }
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String params;
    @JsonIgnore
    private Boolean deleteStatus = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpStreamJobConfId() {
        return upStreamJobConfId;
    }

    public void setUpStreamJobConfId(Long upStreamJobConfId) {
        this.upStreamJobConfId = upStreamJobConfId;
    }

    public Long getPipelineConfId() {
        return pipelineConfId;
    }

    public void setPipelineConfId(Long pipelineConfId) {
        this.pipelineConfId = pipelineConfId;
    }

    public Long getPluginConfId() {
        return pluginConfId;
    }

    public void setPluginConfId(Long pluginConfId) {
        this.pluginConfId = pluginConfId;
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

    public TriggerMode getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(TriggerMode triggerMode) {
        this.triggerMode = triggerMode;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
}
