package com.jlu.jenkins.timer.bean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;

import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.pipeline.job.model.JobBuild;
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
                System.out.println(new SimpleDateFormat().format(new Date()) + " " + buildNumber + " " + isBuilding);
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
            // TODO
            e.printStackTrace();
        } finally {
            // TODO
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
