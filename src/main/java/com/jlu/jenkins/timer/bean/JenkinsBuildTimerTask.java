package com.jlu.jenkins.timer.bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;

/**
 * Created by langshiquan on 18/1/10.
 */
public class JenkinsBuildTimerTask extends TimerTask {

    private IJenkinsBuildService jenkinsBuildService;
    private IJenkinsServerService jenkinsServerService;
    private JenkinsServer jenkinsServer;
    private String jobName;
    private Integer buildNumber;

    public JenkinsBuildTimerTask() {
    }

    public JenkinsBuildTimerTask(IJenkinsBuildService jenkinsBuildService,
                                 IJenkinsServerService jenkinsServerService,
                                 JenkinsServer jenkinsServer, String jobName, Integer buildNumber) {
        this.jenkinsBuildService = jenkinsBuildService;
        this.jenkinsServerService = jenkinsServerService;
        this.jenkinsServer = jenkinsServer;
        this.jobName = jobName;
        this.buildNumber = buildNumber;
    }

    public JenkinsBuildTimerTask setJenkinsBuildService(IJenkinsBuildService jenkinsBuildService) {
        this.jenkinsBuildService = jenkinsBuildService;
        return this;
    }

    public JenkinsBuildTimerTask setJenkinsServerService(
            IJenkinsServerService jenkinsServerService) {
        this.jenkinsServerService = jenkinsServerService;
        return this;
    }

    public JenkinsBuildTimerTask setJenkinsServer(JenkinsServer jenkinsServer) {
        this.jenkinsServer = jenkinsServer;
        return this;
    }

    public JenkinsBuildTimerTask setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public JenkinsBuildTimerTask setBuildNumber(Integer buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    @Override
    public void run() {
        try {
            Build build = jenkinsServerService.getJobBuild(jenkinsServer, jobName, buildNumber);
            BuildWithDetails buildWithDetails = build.details();
            Boolean isBuilding = buildWithDetails.isBuilding();
            if (isBuilding) {
                System.out.println(new SimpleDateFormat().format(new Date()) + " " + buildNumber + " " + isBuilding);
                return;
            } else {
                this.cancel();
                jenkinsBuildService.handleJenkinsJobFinish(jenkinsServer, jobName, buildNumber,
                        buildWithDetails);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
