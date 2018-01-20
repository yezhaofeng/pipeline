package com.jlu.jenkins.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.BuildStatusUtils;
import com.jlu.jenkins.bean.JenkinsBuildDTO;
import com.jlu.jenkins.dao.IJenkinsConfDao;
import com.jlu.jenkins.exception.JenkinsRuntimeException;
import com.jlu.jenkins.exception.JenkinsRuntimeExceptionEnum;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;
import com.jlu.jenkins.timer.service.ITimerService;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.instance.jenkinsjob.service.IJenkinsJobService;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/13.
 */
@Service
public class JenkinsBuildServiceImpl implements IJenkinsBuildService {

    private Logger logger = LoggerFactory.getLogger(JenkinsBuildServiceImpl.class);
    private final Long defaultPerid = 5000L;
    @Autowired
    private IJenkinsServerService jenkinsServerService;

    @Autowired
    private IJenkinsConfDao jenkinsConfDao;
    @Autowired
    private ITimerService timerService;

    @Autowired
    private IJenkinsJobService jenkinsJobService;

    @Deprecated
    @Override
    public Integer buildJob(JenkinsBuildDTO jenkinsBuildDTO) throws IOException {
        String serverUrl = jenkinsBuildDTO.getServerUrl();
        String masterUser = jenkinsBuildDTO.getMasterUser();
        String masterPassword = jenkinsBuildDTO.getMasterPassword();
        String jobName = jenkinsBuildDTO.getJobName();
        String userName = jenkinsBuildDTO.getTriggerUser();
        Map<String, String> params = jenkinsBuildDTO.getParams();
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(serverUrl, masterUser, masterPassword);
        Boolean isExists = jenkinsServerService.isExists(jenkinsServer, jobName);
        if (!isExists) {
            throw new JenkinsRuntimeException(JenkinsRuntimeExceptionEnum.NOT_FOUND_JOB);
        }
        Long lastSuccessfulBuildDuration = jenkinsServerService.getLastSuccessfulBuildDuration(jenkinsServer, jobName);
        Long delay = lastSuccessfulBuildDuration / 2;
        Long period = lastSuccessfulBuildDuration / 20;
        period = period > defaultPerid ? period : defaultPerid;

        Integer buildNumber = jenkinsServerService.build(jenkinsServer, jobName, params);
        JenkinsBuildTimerTask jenkinsBuildTimerTask = new JenkinsBuildTimerTask(this, jenkinsServerService,
                jenkinsServer, jobName, buildNumber);
        timerService.register(jenkinsBuildTimerTask, delay, period);
        return buildNumber;
    }

    @Override
    public Integer buildJob(Long jenkinsServerId, String jobName, Map<String, String> params, JobBuild jobBuild)
            throws IOException {
        JenkinsConf jenkinsConf = jenkinsConfDao.findById(jenkinsServerId);
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(jenkinsConf.getServerUrl(),
                jenkinsConf.getMasterUser(), jenkinsConf.getMasterPassword());
        Boolean isExists = jenkinsServerService.isExists(jenkinsServer, jobName);
        if (!isExists) {
            throw new JenkinsRuntimeException(JenkinsRuntimeExceptionEnum.NOT_FOUND_JOB);
        }
        Long lastSuccessfulBuildDuration = jenkinsServerService.getLastSuccessfulBuildDuration(jenkinsServer, jobName);
        Long delay = lastSuccessfulBuildDuration / 2;
        Long period = lastSuccessfulBuildDuration / 20;
        period = period > defaultPerid ? period : defaultPerid;

        Integer buildNumber = jenkinsServerService.build(jenkinsServer, jobName, params);
        JenkinsBuildTimerTask jenkinsBuildTimerTask = new JenkinsBuildTimerTask(this, jenkinsServerService,
                jenkinsServer, jobName, buildNumber, jobBuild);
        timerService.register(jenkinsBuildTimerTask, delay, period);
        return buildNumber;
    }

    @Override
    public void handleJenkinsJobFinish(JenkinsServer jenkinsServer, String jobName, Integer buildNumber,
                                       BuildWithDetails buildWithDetails, JobBuild jobBuild) throws IOException {
        System.out.println(new SimpleDateFormat().format(new Date()));
        System.out.println(
                jobName + " " + buildNumber + " has fininshed , stastus:" + buildWithDetails.getResult().name());

        switch (jobBuild.getPluginType()) {
            case JENKINS_JOB:
                jobBuild.setJobStatus(BuildStatusUtils.toJobStatus(buildWithDetails.getResult()));
                jobBuild.setEndTime(new Date());
                jenkinsJobService.notifiedJenkinsJobBuildFinished(jobBuild);
                break;
            case COMPILE:
            case RELEASE:
                // TODO: 18/1/20
            default:
                break;
        }

    }

}
