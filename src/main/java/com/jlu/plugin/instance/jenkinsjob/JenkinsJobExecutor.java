package com.jlu.plugin.instance.jenkinsjob;

import java.io.IOException;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsConfService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.jenkins.timer.service.IScheduledService;
import com.offbytwo.jenkins.JenkinsServer;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.pipeline.job.model.JobBuild;
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
    private IJenkinsJobBuildDao jenkinsJobBuildDao;

    @Autowired
    private IJenkinsConfService jenkinsConfService;

    @Autowired
    private IJenkinsServerService jenkinsServerService;

    @Autowired
    private IScheduledService scheduledService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobBuildDao.findById(jobBuild.getPluginBuildId());
        try {
            Long jenkinsServerId = jenkinsJobBuild.getJenkinsServerId();
            String jobName = jenkinsJobBuild.getJobName();
            Integer buildNumber = jenkinsBuildService.buildJob(jenkinsServerId, jobName, jobBuild
                    .getInParameterMap(), jobBuild);
//            System.out.println("jobBuildId-" + jobBuild.getId() + " buildNumber-" + buildNumber);
            logger.info("jobBuildId-{} buildNumber-{}", jobBuild.getId(), buildNumber);
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(jenkinsJobBuild.getJobFullName()).append("/")
                    .append("/").append(buildNumber)
                    .append("/").append("console");
            jenkinsJobBuild.setBuildUrl(logUrl.toString());
            jenkinsJobBuild.setBuildNumber(buildNumber);
            jenkinsJobBuildDao.saveOrUpdate(jenkinsJobBuild);
        } catch (IOException ioe) {
            notifyJobStartFailed(jobBuild, "通讯异常");
            return;
        } catch (JenkinsException jre) {
            notifyJobStartFailed(jobBuild, jre.getMessage());
            return;
        } catch (Exception e) {
            logger.error("jenkins job build unkown error", e);
            notifyJobStartFailed(jobBuild, "UnKnown Error:" + e.getMessage());
            return;
        }
        notifyJobStartSucc(jobBuild);
    }

    @Override
    public void cancel(JobBuild jobBuild) {
        Long pluginBuildId = jobBuild.getPluginBuildId();
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobBuildDao.findById(pluginBuildId);
        Long jenkinsServerId = jenkinsJobBuild.getJenkinsServerId();
        Integer buildNumber = jenkinsJobBuild.getBuildNumber();
        String jobName = jenkinsJobBuild.getJobName();
        JenkinsConf jenkinsConf = jenkinsConfService.get(jenkinsServerId);
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(jenkinsConf);
        try {
            jenkinsServerService.cancel(jenkinsServer, jobName, buildNumber);
        } catch (IOException e) {
            throw new PipelineRuntimeException("网络异常");
        }
        scheduledService.cancel(jobBuild);
    }
}
