package com.jlu.plugin.instance.jenkinsjob;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsRuntimeException;
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
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(File.separator)
                    .append("job").append(File.separator).append(jenkinsJobBuild.getJobName())
                    .append(File.separator).append(buildNumber)
                    .append(File.separator).append("console");
            jenkinsJobBuild.setBuildUrl(logUrl.toString());
            jenkinsJobDao.saveOrUpdate(jenkinsJobBuild);
            notifyJobStartSucc(jobBuild);
        } catch (IOException ioe) {
            notifyJobStartFailed(jobBuild, "通讯异常");
        } catch (JenkinsRuntimeException jre) {
            notifyJobStartFailed(jobBuild, jre.getMessage());
        } catch (Exception e) {
            logger.error("jenkins job error", e);
            notifyJobStartFailed(jobBuild, "UnKnown Error:" + e.getMessage());
        }
        notifyJobStartSucc(jobBuild);
    }
}
