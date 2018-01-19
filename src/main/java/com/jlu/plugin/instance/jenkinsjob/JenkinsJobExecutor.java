package com.jlu.plugin.instance.jenkinsjob;

import org.springframework.stereotype.Service;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.IExecutor;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobExecutor extends IExecutor {

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        //        jobBuild.setJobStatus(PipelineJobStatus.SUCCESS);
        //        jobBuildService.notifiedJobBuildFinished(jobBuild);
    }
}
