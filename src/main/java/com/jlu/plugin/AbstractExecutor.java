package com.jlu.plugin;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Job executor
 */
public abstract class AbstractExecutor {

    @Autowired
    protected IJobBuildService jobBuildService;

    public void executeJob(JobBuildContext context, JobBuild jobBuild) {
        jobBuild.setStartTime(new Date());
        jobBuildService.saveOrUpdate(jobBuild);
        Long pluginBuildId = jobBuild.getPluginBuildId();
        if (pluginBuildId == null || pluginBuildId == -1L || pluginBuildId == 0L) {
            return;
        }
        execute(context, jobBuild);
    }

    protected abstract void execute(JobBuildContext context, JobBuild jobBuild);

    public void handleCallback(JobBuild jobBuild) {
        jobBuildService.notifiedJobBuildFinished(jobBuild, new HashMap());
    }
}
