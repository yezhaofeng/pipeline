package com.jlu.plugin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Job executor
 */
public abstract class IExecutor {

    @Autowired
    protected IJobBuildService jobBuildService;

    public void executeJob(JobBuildContext context, JobBuild jobBuild) {
        jobBuild.setStartTime(new Date());
        jobBuildService.saveOrUpdate(jobBuild);
        execute(context, jobBuild);
    }

    public abstract void execute(JobBuildContext context, JobBuild jobBuild);

}
