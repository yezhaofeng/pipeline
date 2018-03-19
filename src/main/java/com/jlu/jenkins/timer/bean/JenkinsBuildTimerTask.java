package com.jlu.jenkins.timer.bean;

import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.pipeline.job.model.JobBuild;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/10.
 */
@Deprecated
public class JenkinsBuildTimerTask extends TimerTask {

    private Logger logger = LoggerFactory.getLogger(JenkinsBuildTimerTask.class);
    private Integer ioErrorTimes = 0;
    // 允许发生异常的次数
    private final Integer maxIoErrorTimes = 5;
    private IJenkinsBuildService jenkinsBuildService;
    private IJenkinsServerService jenkinsServerService;
    private JenkinsServer jenkinsServer;
    private String jobName;
    private Integer buildNumber;
    private JobBuild jobBuild;
    private Vector vector;

    public JenkinsBuildTimerTask() {
    }

    public JenkinsBuildTimerTask(IJenkinsBuildService jenkinsBuildService,
                                 IJenkinsServerService jenkinsServerService,
                                 JenkinsServer jenkinsServer, String jobName, Integer buildNumber, JobBuild jobBuild) {
        this.jenkinsBuildService = jenkinsBuildService;
        this.jenkinsServerService = jenkinsServerService;
        this.jenkinsServer = jenkinsServer;
        this.jobName = jobName;
        this.buildNumber = buildNumber;
        this.jobBuild = jobBuild;
    }

    public JobBuild getJobBuild() {
        return jobBuild;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    @Override
    public void run() {
        try {
            Build build = jenkinsServerService.getJobBuild(jenkinsServer, jobName, buildNumber);
            BuildWithDetails buildWithDetails = build.details();
            Boolean isBuilding = buildWithDetails.isBuilding();
            if (isBuilding) {
                logger.info("jobBuildId-{} jobName-{} buildNumber-{} is building", jobBuild.getId(), jobName,
                        buildNumber);
                return;
            } else {
                this.cancel();
                if (vector != null) {
                    vector.remove(this.getJobBuild());
                }
                jenkinsBuildService.handleJenkinsJobFinish(jenkinsServer, jobName, buildNumber,
                        buildWithDetails, jobBuild);
            }
        } catch (IOException e) {
            logger.warn("jobBuildId-{} jobName-{} buildNumber-{} occur IOException {} times,html.error:", jobBuild.getId
                    (), jobName, buildNumber, ++ioErrorTimes, e);
            if (ioErrorTimes >= maxIoErrorTimes) {
                this.cancel();
                if (vector != null) {
                    vector.remove(this.getJobBuild());
                }
                try {
                    jenkinsBuildService.handleJenkinsJobFinish(jenkinsServer, jobName, buildNumber,
                            null, jobBuild);
                } catch (Exception ce) {
                    logger.info("jobBuildId-{} jobName-{} buildNumber-{} notify IOException message occur Exception,"
                            + "html.error:", jobBuild.getId(), jobName, buildNumber, ce);
                }
            }

        } catch (Exception e) {
            logger.info("jobBuildId-{} jobName-{} buildNumber-{} occur Exception,error:", jobBuild.getId(), jobName,
                    buildNumber, e);
            this.cancel();
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("JenkinsBuildTimerTask{");
        sb.append("jobName='").append(jobName).append('\'');
        sb.append(", buildNumber=").append(buildNumber);
        sb.append(", jobBuild=").append(jobBuild);
        sb.append('}');
        return sb.toString();
    }
}
