package com.jlu.pipeline.job.bean;

import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
public class JobBuildBean extends JobBuild{
    Object pluginBuild;

    public Object getPluginBuild() {
        return pluginBuild;
    }

    public void setPluginBuild(Object pluginBuild) {
        this.pluginBuild = pluginBuild;
    }
}
