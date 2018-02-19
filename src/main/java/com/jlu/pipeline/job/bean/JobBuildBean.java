package com.jlu.pipeline.job.bean;

import com.jlu.common.utils.DateUtils;
import com.jlu.pipeline.job.model.JobBuild;

import java.util.Date;

/**
 * Created by langshiquan on 18/1/14.
 */
public class JobBuildBean extends JobBuild {
    Object pluginBuild;
    public Boolean buildable = false;

    public Object getPluginBuild() {
        return pluginBuild;
    }

    public void setPluginBuild(Object pluginBuild) {
        this.pluginBuild = pluginBuild;
    }

    public Boolean getBuildable() {
        return buildable;
    }

    public void setBuildable(Boolean buildable) {
        this.buildable = buildable;
    }

    public String getDurationTime() {
        switch (jobStatus) {
            case SUCCESS:
            case FAILED:
                return DateUtils.getRealableChineseTime(endTime.getTime() - triggerTime.getTime());
            case INIT:
                return null;
            case RUNNING:
                return DateUtils.getRealableChineseTime(new Date().getTime() - triggerTime.getTime());
            default:
                return null;
        }
    }

    public Integer getRunPercentage() {
        switch (super.jobStatus) {
            case SUCCESS:
            case FAILED:
                return 100;
            case INIT:
                return 0;
            case RUNNING:
                // TODO 进度条
                return 90;
            default:
                return 0;
        }
    }

}
