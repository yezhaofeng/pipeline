package com.jlu.plugin.instance.compile.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by langshiquan on 18/1/20.
 */
@Entity
public class CompileBuild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String logUrl;

    private String buildPath;

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

    public String getBuildPath() {
        return buildPath;
    }

    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductWgetCommand() {
        return buildPath;
    }
}
