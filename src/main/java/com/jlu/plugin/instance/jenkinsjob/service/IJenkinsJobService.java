package com.jlu.plugin.instance.jenkinsjob.service;

import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/20.
 */
public interface IJenkinsJobService {
    void notifiedJenkinsJobBuildFinished(JobBuild jobBuild);
}
