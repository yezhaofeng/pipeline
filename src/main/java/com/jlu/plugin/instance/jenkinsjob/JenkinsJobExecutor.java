package com.jlu.plugin.instance.jenkinsjob;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsConfService;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.IExecutor;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobExecutor extends IExecutor {

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private IJenkinsConfService jenkinsConfService;

    @Autowired
    private IJenkinsJobBuildDao jenkinsJobDao;

    @Autowired
    private IJobBuildService jobBuildService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobDao.findById(jobBuild.getPluginBuildId());

        try {
            jenkinsBuildService.buildJob(jenkinsJobBuild.getJenkinsServerId(), jenkinsJobBuild.getJobName(), jobBuild
                    .getInParameterMap(), jobBuild);
        } catch (IOException e) {
            e.printStackTrace();
        }
        jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        jobBuildService.saveOrUpdate(jobBuild);
    }
}
