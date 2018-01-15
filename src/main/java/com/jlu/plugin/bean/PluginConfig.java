package com.jlu.plugin.bean;

/**
 * Created by baidu on 15/10/22.
 */
public class PluginConfig {

    protected PluginType pluginType;

    protected String name;

    protected String description;

    protected String link;

    protected Integer priority = 0;

    protected PluginPage configPage;

    protected PluginPage buildPage;

    protected PluginPage buildBriefPage;

    protected PluginPage executeRequirePage;

    protected String icon;

    public PluginConfig() {
    }

    public PluginConfig(PluginType pluginType, String name) {
        this.pluginType = pluginType;
        this.name = name;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public void setPluginType(PluginType pluginType) {
        this.pluginType = pluginType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public PluginPage getConfigPage() {
        return configPage;
    }

    public void setConfigPage(PluginPage configPage) {
        this.configPage = configPage;
    }

    public PluginPage getBuildPage() {
        return buildPage;
    }

    public void setBuildPage(PluginPage buildPage) {
        this.buildPage = buildPage;
    }

    public PluginPage getBuildBriefPage() {
        return buildBriefPage;
    }

    public void setBuildBriefPage(PluginPage buildBriefPage) {
        this.buildBriefPage = buildBriefPage;
    }

    public PluginPage getExecuteRequirePage() {
        return executeRequirePage;
    }

    public void setExecuteRequirePage(PluginPage executeRequirePage) {
        this.executeRequirePage = executeRequirePage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
