package com.jlu.jenkins.service;

import java.io.IOException;

import com.jlu.jenkins.bean.JenkinsBuildDTO;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/13.
 */
public interface IJenkinsBuildService {
    Integer buildJob(JenkinsBuildDTO jenkinsBuildDTO) throws IOException;

    void handleJenkinsJobFinish(JenkinsServer jenkinsServer, String jobName, Integer buildNumber,
                                BuildWithDetails buildWithDetails) throws IOException;
}
