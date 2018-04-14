package com.jlu.jenkins.service;

import java.io.IOException;
import java.util.Map;

import com.jlu.jenkins.model.JenkinsBuild;
import com.jlu.pipeline.job.model.JobBuild;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/13.
 */
public interface IJenkinsBuildService {

    Integer buildJob(Long jenkinsConfId, String jobName, Map<String, String> params, JobBuild jobBuild)
            throws IOException;

    void handleJenkinsJobFinish(JenkinsServer jenkinsServer, String jobName, Integer buildNumber,
                                BuildWithDetails buildWithDetails, JobBuild jobBuild, JenkinsBuild jenkinsBuild) throws IOException;
}
