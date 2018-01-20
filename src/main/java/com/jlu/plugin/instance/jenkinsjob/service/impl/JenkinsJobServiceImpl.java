package com.jlu.plugin.instance.jenkinsjob.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.instance.jenkinsjob.service.IJenkinsJobService;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class JenkinsJobServiceImpl implements IJenkinsJobService {
    @Autowired
    private IJobBuildService jobBuildService;

    @Override
    public void notifiedJenkinsJobBuildFinished(JobBuild jobBuild) {
        jobBuildService.notifiedJobBuildFinished(jobBuild, new HashMap());
    }
}
