package com.jlu.jenkins.service.impl;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.exception.JenkinsExceptionEnum;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;
import com.jlu.jenkins.timer.service.ITimerService;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.service.IPluginInfoService;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/13.
 */
@Service
public class JenkinsBuildServiceImpl implements IJenkinsBuildService {

    private Logger logger = LoggerFactory.getLogger(JenkinsBuildServiceImpl.class);
    private final static Long DEFAULT_PERIOD = 5000L;
    @Autowired
    private IJenkinsServerService jenkinsServerService;

    @Autowired
    private IJenkinsConfDao jenkinsConfDao;

    @Autowired
    private ITimerService timerService;

    @Autowired
    private IPluginInfoService pluginInfoService;


    @Override
    public Integer buildJob(Long jenkinsServerId, String jobName, Map<String, String> params, JobBuild jobBuild)
            throws IOException {
        JenkinsConf jenkinsConf = jenkinsConfDao.findById(jenkinsServerId);
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(jenkinsConf.getServerUrl(),
                jenkinsConf.getMasterUser(), jenkinsConf.getMasterPassword());
        Boolean isExists = jenkinsServerService.isExists(jenkinsServer, jobName);
        if (!isExists) {
            throw new JenkinsException(JenkinsExceptionEnum.NOT_FOUND_JOB);
        }
        Long lastSuccessfulBuildDuration = jenkinsServerService.getLastSuccessfulBuildDuration(jenkinsServer, jobName);
        Long delay = lastSuccessfulBuildDuration / 2;
        Long period = lastSuccessfulBuildDuration / 10;
        period = period > DEFAULT_PERIOD ? period : DEFAULT_PERIOD;

        Integer buildNumber = jenkinsServerService.build(jenkinsServer, jobName, params);
        JenkinsBuildTimerTask jenkinsBuildTimerTask = new JenkinsBuildTimerTask(this, jenkinsServerService,
                jenkinsServer, jobName, buildNumber, jobBuild);
        timerService.register(jenkinsBuildTimerTask, delay, period);
        return buildNumber;
    }

    @Override
    public void handleJenkinsJobFinish(JenkinsServer jenkinsServer, String jobName, Integer buildNumber,
                                       BuildWithDetails buildWithDetails, JobBuild jobBuild) throws IOException {
        if (buildWithDetails == null) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage(JenkinsExceptionEnum.NETWORK_UNREACHABLE.name());
            pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getExecutor().handleCallback(jobBuild);
            return;
        }
        BuildResult buildResult = buildWithDetails.getResult();
        logger.info("jobBuildId-{} {} {} has finished,status:{}", jobBuild.getId(), jobName, buildNumber,
                buildResult.name());
        jobBuild.setJobStatus(PipelineJobStatus.fromJenkinsBuildStatus(buildResult));
        pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getExecutor().handleCallback(jobBuild);
    }

}
