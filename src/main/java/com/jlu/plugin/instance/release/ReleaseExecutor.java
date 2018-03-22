package com.jlu.plugin.instance.release;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.service.DefaultJenkinsServer;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.pipeline.job.bean.JobParameter;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.pipeline.model.PipelineBuild;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.instance.release.model.ReleaseBuild;
import com.jlu.plugin.instance.release.service.IReleaseService;

/**
 * Created by langshiquan on 18/1/20.
 */
@Service
public class ReleaseExecutor extends AbstractExecutor {

    private Logger logger = LoggerFactory.getLogger(ReleaseExecutor.class);

    private final static String SEPARATOR = "/";
    private final static String RELEASE_JENKINS_JOB_NAME = "release";
    private final static String TEMP_PRODUCT_SOURCE_LOCATION = "TEMP_PRODUCT_SOURCE_LOCATION";
    private final static String RELEASE_PRODUCT_TARGET_LOCATION = "RELEASE_PRODUCT_TARGET_LOCATION";
    private final static String FTP_SERVER_URL = "ftp://139.199.15.115/";

    private final static String VERSION_LESS_MESSAGE = "版本号不能减小";
    private final static String VERSION_FORMAT_ERROR_MESSAGE = "版本号格式不正确";

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private IReleaseService releaseService;

    @Override
    protected void execute(JobBuildContext context, JobBuild jobBuild) {
        ReleaseBuild releaseBuild = releaseService.get(jobBuild.getPluginBuildId());
        PipelineBuild pipelineBuild = context.getPipelineBuild();
        Map<String, String> inParams = jobBuild.getInParameterMap();
        String compileProductFtpPath = inParams.get(JobParameter.PIPELINE_COMPILE_PRODUCT_PATH);
        if (StringUtils.isBlank(compileProductFtpPath)) {
            notifyJobStartFailed(jobBuild, "未发现编译产出，请在发版Job之前配置构建Job");
            return;
        }
        try {
            String module = pipelineBuild.getModule();
            String compileProductLocation = getCompileLocation(compileProductFtpPath);
            String maxVersion = releaseService.getMaxVersion(module);
            String version = releaseBuild.getVersion();
            if (StringUtils.isBlank(version)) {
                version = releaseService.increaseVersion(maxVersion);
            }
            if (!releaseService.checkVersion(version)) {
                releaseBuild.setStatus(PipelineJobStatus.FAILED);
                releaseBuild.setMessage(VERSION_FORMAT_ERROR_MESSAGE);
                releaseService.saveOrUpdate(releaseBuild);
                notifyJobStartFailed(jobBuild, VERSION_FORMAT_ERROR_MESSAGE);
                return;
            }
            if (!releaseService.compareVersion(version, maxVersion)) {
                releaseBuild.setStatus(PipelineJobStatus.FAILED);
                releaseBuild.setMessage(VERSION_LESS_MESSAGE);
                releaseService.saveOrUpdate(releaseBuild);
                notifyJobStartFailed(jobBuild, VERSION_LESS_MESSAGE);
                return;
            }

            StringBuilder releaseTargetLocation = new StringBuilder();
            releaseTargetLocation.append("release").append(SEPARATOR)
                    .append(module).append(SEPARATOR)
                    .append(version.replace(".", "_"));
            HashMap<String, String> releaseParams = new HashMap<>();
            releaseParams.put(TEMP_PRODUCT_SOURCE_LOCATION, compileProductLocation);
            releaseParams.put(RELEASE_PRODUCT_TARGET_LOCATION, releaseTargetLocation.toString());

            Integer buildNumber = jenkinsBuildService
                    .buildJob(DefaultJenkinsServer.ID, RELEASE_JENKINS_JOB_NAME, releaseParams, jobBuild);

            StringBuilder releasePath = new StringBuilder();
            releasePath.append(FTP_SERVER_URL).append(module)
                    .append(SEPARATOR).append(version.replace(".", "_"));
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL)
                    .append("job").append(SEPARATOR).append(RELEASE_JENKINS_JOB_NAME)
                    .append(SEPARATOR).append(buildNumber)
                    .append(SEPARATOR).append("console");
            releaseBuild.setReleasePath(releasePath.toString());
            releaseBuild.setLogUrl(logUrl.toString());
            releaseBuild.setModule(module);
            releaseBuild.setVersion(version);
            releaseBuild.setBranch(pipelineBuild.getBranch());
            releaseBuild.setCommitId(pipelineBuild.getCommitId());
            releaseBuild.setTriggerUser(jobBuild.getTriggerUser());
            releaseBuild.setStatus(PipelineJobStatus.RUNNING);
            releaseBuild.setPipelineName(pipelineBuild.getName());
            releaseBuild.setBuildNumber(buildNumber);
            releaseService.saveOrUpdate(releaseBuild);
            jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        } catch (IOException ioe) {
            notifyJobStartFailed(jobBuild, "网络异常");
            return;
        } catch (JenkinsException jre) {
            notifyJobStartFailed(jobBuild, jre.getMessage());
            return;
        } catch (Exception e) {
            logger.error("release occur unknown error,", e);
            notifyJobStartFailed(jobBuild, "UnKnown Error:" + e.getMessage());
            return;
        }
        notifyJobStartSucc(jobBuild);
    }

    private String getCompileLocation(String compileProductFtpPath) {
        // "ftp://139.199.15.115" 长度为20
        return "snapshot" + SEPARATOR + compileProductFtpPath.substring(FTP_SERVER_URL.length(), compileProductFtpPath.length());
    }

    @Override
    public void handleCallback(JobBuild jobBuild) {
        ReleaseBuild releaseBuild = releaseService.get(jobBuild.getPluginBuildId());
        releaseBuild.setStatus(jobBuild.getJobStatus());
        releaseService.saveOrUpdate(releaseBuild);
        Map<String, String> newParams = new HashedMap();
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)) {
            newParams.put(JobParameter.PIPELINE_RELEASE_PRODUCT_PATH, releaseBuild.getReleasePath());
            newParams.put(JobParameter.PIPELINE_RELEASE_VERSION, releaseBuild.getVersion());
        }
        if (PipelineJobStatus.SUCCESS.equals(jobBuild.getJobStatus())) {
            jobBuild.setName(jobBuild.getName() + " " + releaseBuild.getVersion());
        }
        jobBuildService.notifiedJobBuildFinished(jobBuild, newParams);
    }
}
