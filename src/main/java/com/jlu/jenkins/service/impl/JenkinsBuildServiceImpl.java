package com.jlu.jenkins.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.jlu.jenkins.dao.IJenkinsBuildDao;
import com.jlu.jenkins.model.JenkinsBuild;
import com.jlu.jenkins.timer.bean.JenkinsBuildScheduledTask;
import com.jlu.jenkins.timer.service.IScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private IScheduledService scheduledService;

    @Autowired
    private IPluginInfoService pluginInfoService;

    @Autowired
    private IJenkinsBuildDao jenkinsBuildDao;

    @Override
    public Integer buildJob(Long jenkinsConfId, String jobName, Map<String, String> params, JobBuild jobBuild)
            throws IOException {
        JenkinsConf jenkinsConf = jenkinsConfDao.findById(jenkinsConfId);
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

        JenkinsBuild jenkinsBuild = new JenkinsBuild();
        jenkinsBuild.setStartTime(new Date());
        jenkinsBuild.setBuildNumber(buildNumber);
        jenkinsBuild.setJenkinsConfId(jenkinsConfId);
        jenkinsBuild.setPipelineJobBuildId(jobBuild.getId());
        jenkinsBuild.setJobName(jobName);
        jenkinsBuildDao.saveOrUpdate(jenkinsBuild);
        JenkinsBuildScheduledTask jenkinsBuildScheduledTask = new JenkinsBuildScheduledTask(jenkinsServer, jobName, buildNumber, jobBuild, jenkinsBuild);
        scheduledService.register(jenkinsBuildScheduledTask, delay, period);
        return buildNumber;
    }

    @Override
    public void handleJenkinsJobFinish(JenkinsServer jenkinsServer, String jobName, Integer buildNumber,
                                       BuildWithDetails buildWithDetails, JobBuild jobBuild, JenkinsBuild jenkinsBuild) throws IOException {
        if (buildWithDetails == null) {
            jenkinsBuild.setBuildResult(BuildResult.UNKNOWN);
            jenkinsBuild.setEndTime(new Date());
            jenkinsBuildDao.saveOrUpdate(jenkinsBuild);

            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage(JenkinsExceptionEnum.NETWORK_UNREACHABLE.name());
            pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getExecutor().handleCallback(jobBuild);
            return;
        }

        BuildResult buildResult = buildWithDetails.getResult();
        jenkinsBuild.setBuildResult(buildResult);
        jenkinsBuild.setRealEndTime(buildWithDetails.getTimestamp());
        jenkinsBuildDao.saveOrUpdate(jenkinsBuild);
        logger.info("jobBuildId-{} {} {} has finished,status:{}", jobBuild.getId(), jobName, buildNumber,
                buildResult.name());
        jobBuild.setJobStatus(PipelineJobStatus.fromJenkinsBuildStatus(buildResult));
        pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getExecutor().handleCallback(jobBuild);
    }

}
