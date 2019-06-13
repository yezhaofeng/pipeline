package com.jlu.jenkins.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jlu.jenkins.dao.IJenkinsBuildDao;
import com.jlu.jenkins.model.JenkinsBuild;
import com.jlu.jenkins.timer.bean.JenkinsBuildScheduledTask;
import com.jlu.jenkins.timer.service.IScheduledService;
import com.jlu.pipeline.job.dao.IJobBuildDao;
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

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.JobName;

/**
 * Created by yezhaofeng on 2019/1/13.
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
    private IJobBuildDao jobBuildDao;

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

        Integer buildNumber = jenkinsServerService.build(jenkinsServer, jobName, params);
        JenkinsBuild jenkinsBuild = new JenkinsBuild();
        jenkinsBuild.setStartTime(new Date());
        jenkinsBuild.setBuildNumber(buildNumber);
        jenkinsBuild.setJenkinsConfId(jenkinsConfId);
        jenkinsBuild.setPipelineJobBuildId(jobBuild.getId());
        jenkinsBuild.setJobName(jobName);
        jenkinsBuildDao.saveOrUpdate(jenkinsBuild);
        registerPollingJenkinsBuild(jenkinsServer, jobName, buildNumber, jobBuild, jenkinsBuild);
        return buildNumber;
    }

    private void registerPollingJenkinsBuild(JenkinsServer jenkinsServer, String jobName, Integer buildNumber, JobBuild jobBuild, JenkinsBuild jenkinsBuild) throws IOException {
        JenkinsBuildScheduledTask jenkinsBuildScheduledTask = new JenkinsBuildScheduledTask(jenkinsServer, jobName, buildNumber, jobBuild, jenkinsBuild);
        Long lastSuccessfulBuildDuration = jenkinsServerService.getLastSuccessfulBuildDuration(jenkinsServer, jobName);
        Long delay = lastSuccessfulBuildDuration / 2;
        Long period = lastSuccessfulBuildDuration / 10;
        period = period > DEFAULT_PERIOD ? period : DEFAULT_PERIOD;
        scheduledService.register(jenkinsBuildScheduledTask, delay, period);
    }

    @PostConstruct
    public void pollRunningJobInDb() {
        List<JenkinsBuild> jenkinsBuildList = jenkinsBuildDao.findWithoutBuildResult();
        for (JenkinsBuild jenkinsBuild : jenkinsBuildList) {
            try {
                Long jenkinsConfId = jenkinsBuild.getJenkinsConfId();
                JenkinsConf jenkinsConf = jenkinsConfDao.findById(jenkinsConfId);
                JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(jenkinsConf);
                String jobName = jenkinsBuild.getJobName();
                Integer buildNumber = jenkinsBuild.getBuildNumber();
                Long jobBuildId = jenkinsBuild.getPipelineJobBuildId();
                JobBuild jobBuild = jobBuildDao.findById(jobBuildId);
                registerPollingJenkinsBuild(jenkinsServer, jobName, buildNumber, jobBuild, jenkinsBuild);
            } catch (Exception e) {
                logger.warn("postConstruct:{} error", jenkinsBuild, e);
                continue;
            }
        }
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
        jenkinsBuild.setEndTime(new Date());
        jenkinsBuildDao.saveOrUpdate(jenkinsBuild);
        logger.info("jobBuildId-{} {} {} has finished,status:{}", jobBuild.getId(), jobName, buildNumber,
                buildResult.name());
        jobBuild.setJobStatus(PipelineJobStatus.fromJenkinsBuildStatus(buildResult));
        pluginInfoService.getRealJobPlugin(jobBuild.getPluginType()).getExecutor().handleCallback(jobBuild);
    }


}
