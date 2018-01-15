package com.jlu.plugin;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Job executor
 */
public interface IExecutor {

    void execute(JobBuildContext context, JobBuild jobBuild);

}
