package com.jlu.plugin.instance.jenkinsjob;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.service.DefaultJenkinsServer;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobExecutor extends AbstractExecutor {

    private Logger logger = LoggerFactory.getLogger(JenkinsJobExecutor.class);
    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private IJenkinsJobBuildDao jenkinsJobDao;

    @Autowired
    private IJobBuildService jobBuildService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobDao.findById(jobBuild.getPluginBuildId());
        try {
            Integer buildNumber = jenkinsBuildService.buildJob(jenkinsJobBuild.getJenkinsServerId(), jenkinsJobBuild
                    .getJobName(), jobBuild
                    .getInParameterMap(), jobBuild);
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(jenkinsJobBuild.getJobFullName()).append("/")
                    .append("/").append(buildNumber)
                    .append("/").append("console");
            jenkinsJobBuild.setBuildUrl(logUrl.toString());
            jenkinsJobDao.saveOrUpdate(jenkinsJobBuild);
            notifyJobStartSucc(jobBuild);
        } catch (IOException ioe) {
            notifyJobStartFailed(jobBuild, "通讯异常");
            return;
        } catch (JenkinsException jre) {
            notifyJobStartFailed(jobBuild, jre.getMessage());
            return;
        } catch (Exception e) {
            logger.error("jenkins job html.error", e);
            notifyJobStartFailed(jobBuild, "UnKnown Error:" + e.getMessage());
            return;
        }
        notifyJobStartSucc(jobBuild);
    }
}
