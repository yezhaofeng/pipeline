package com.jlu.jenkins.timer.bean;

import com.jlu.common.service.ServiceBeanFactory;
import com.jlu.jenkins.model.JenkinsBuild;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.pipeline.job.model.JobBuild;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public class JenkinsBuildScheduledTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(JenkinsBuildScheduledTask.class);
    private Integer ioErrorTimes = 0;
    // 允许发生异常的次数
    private final Integer maxIoErrorTimes = 5;
    private JenkinsServer jenkinsServer;
    private String jobName;
    private Integer buildNumber;
    private JobBuild jobBuild;
    private JenkinsBuild jenkinsBuild;

    public JenkinsBuildScheduledTask(JenkinsServer jenkinsServer, String jobName, Integer buildNumber, JobBuild jobBuild, JenkinsBuild jenkinsBuild) {
        this.jenkinsServer = jenkinsServer;
        this.jobName = jobName;
        this.buildNumber = buildNumber;
        this.jobBuild = jobBuild;
        this.jenkinsBuild = jenkinsBuild;
    }

    public JobBuild getJobBuild() {
        return jobBuild;
    }

    @Override
    public void run() {
        try {
            logger.info("jenkinsServer.getJob(jobName).details()-{}",jenkinsServer.getJob(jobName).details());
            logger.info("jenkinsServer.getJob(jobName).details().getBuildByNumber(buildNumber)-{}",jenkinsServer.getJob(jobName).details().getBuildByNumber(buildNumber));
            //bug 新版Jenkins API和offbytwo中不一致 导致无法通过buildNumber获得build
/*            Build build = ServiceBeanFactory.getJenkinsServerService().getJobBuild(jenkinsServer, jobName, buildNumber);
            BuildWithDetails buildWithDetails = build.details();
            Boolean isBuilding = buildWithDetails.isBuilding();*/
            Build build = ServiceBeanFactory.getJenkinsServerService().getJobBuild(jenkinsServer, jobName, buildNumber);
            Boolean isBuilding = false;
            BuildWithDetails buildWithDetails = null;
            if(build == null){
                //build获取不到时，默认为成功
                buildWithDetails = new BuildWithDetails();
                buildWithDetails.setResult(BuildResult.SUCCESS);
            }else{
                buildWithDetails = build.details();
                isBuilding = buildWithDetails.isBuilding();
            }
            if (isBuilding) {
                logger.info("jobBuildId-{} jobName-{} buildNumber-{} is building", jobBuild.getId(), jobName,
                        buildNumber);
                return;
            } else {
                ServiceBeanFactory.getJenkinsBuildService().handleJenkinsJobFinish(jenkinsServer, jobName, buildNumber,
                        buildWithDetails, jobBuild, jenkinsBuild);
                cancel();
            }
        } catch (IOException e) {
            handleNetWorkException(e);
        } catch (Exception e) {
            handleUnKownException(e);
        }

    }

    private void handleNetWorkException(IOException e) {
        logger.warn("jobBuildId-{} jobName-{} buildNumber-{} occur IOException {} times,html.error:", jobBuild.getId
                (), jobName, buildNumber, ++ioErrorTimes, e);
        if (ioErrorTimes >= maxIoErrorTimes) {
            try {
                ServiceBeanFactory.getJenkinsBuildService().handleJenkinsJobFinish(jenkinsServer, jobName, buildNumber,
                        null, jobBuild, jenkinsBuild);
            } catch (Exception ce) {
                logger.info("jobBuildId-{} jobName-{} buildNumber-{} notify IOException message occur Exception,"
                        + "html.error:", jobBuild.getId(), jobName, buildNumber, ce);
            }
            cancel();
        }
    }

    private void handleUnKownException(Exception e) {
        logger.info("jobBuildId-{} jobName-{} buildNumber-{} occur Exception,error:", jobBuild.getId(), jobName,
                buildNumber, e);
        cancel();
    }

    private void cancel() {
        ServiceBeanFactory.getScheduledService().cancel(jobBuild);
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
