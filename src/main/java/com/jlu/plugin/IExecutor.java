package com.jlu.plugin;

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

    public abstract void execute(JobBuildContext context, JobBuild jobBuild);

}
