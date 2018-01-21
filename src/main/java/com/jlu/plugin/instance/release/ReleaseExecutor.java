package com.jlu.plugin.instance.release;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsRuntimeException;
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

    private final static String RELEASE_JENKINS_JOB_NAME = "release";
    private final static String TEMP_PRODUCT_SOURCE_LOCATION = "TEMP_PRODUCT_SOURCE_LOCATION";
    private final static String RELEASE_PRODUCT_TARGET_LOCATION = "RELEASE_PRODUCT_TARGET_LOCATION";
    private final static String FTP_SERVER_URL = "ftp://139.199.15.115/";

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private IReleaseService releaseService;


    @Override
    protected void execute(JobBuildContext context, JobBuild jobBuild) {
        ReleaseBuild releaseBuild = releaseService.find(jobBuild.getPluginBuildId());
        PipelineBuild pipelineBuild = context.getPipelineBuild();
        Map<String, String> inParams = jobBuild.getInParameterMap();
        String compileProductFtpPath = inParams.get(JobParameter.PIPELINE_COMPILE_PRODUCT_PATH);
        if (StringUtils.isBlank(compileProductFtpPath)) {
            jobBuild.setMessage("未发现编译产出，请在发版Job之前配置构建Job");
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuildService.notifiedJobBuildUpdated(jobBuild, new HashedMap());
            return;
        }
        String owner = pipelineBuild.getOwner();
        String module = pipelineBuild.getModule();
        String compileProductLocation = getCompileLocation(compileProductFtpPath);
        String version = releaseService.getNextVersion(owner, module);
        StringBuilder releaseTargetLocation = new StringBuilder();
        releaseTargetLocation.append("release").append(File.separator)
                .append(owner).append(File.separator)
                .append(module).append(File.separator)
                .append(version.replace(".", "_"));
        HashMap<String, String> releaseParams = new HashMap<>();
        releaseParams.put(TEMP_PRODUCT_SOURCE_LOCATION, compileProductLocation);
        releaseParams.put(RELEASE_PRODUCT_TARGET_LOCATION, releaseTargetLocation.toString());
        try {
            Integer buildNumber = jenkinsBuildService
                    .buildJob(DefaultJenkinsServer.ID, RELEASE_JENKINS_JOB_NAME, releaseParams, jobBuild);

            StringBuilder releasePath = new StringBuilder();
            releasePath.append(FTP_SERVER_URL).append(owner)
                    .append(File.separator).append(module)
                    .append(File.separator).append(version.replace(".", "_"));
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(File.separator)
                    .append("job").append(File.separator).append(RELEASE_JENKINS_JOB_NAME)
                    .append(File.separator).append(buildNumber)
                    .append(File.separator).append("console");
            releaseBuild.setReleasePath(releasePath.toString());
            releaseBuild.setLogUrl(logUrl.toString());
            releaseBuild.setOwner(owner);
            releaseBuild.setModule(module);
            releaseBuild.setBranch(pipelineBuild.getBranch());
            releaseBuild.setCommitId(pipelineBuild.getCommitId());
            releaseBuild.setTriggerUser(jobBuild.getTriggerUser());
            releaseBuild.setStatus(PipelineJobStatus.RUNNING);
            releaseBuild.setPipelineName(pipelineBuild.getName());
            releaseService.saveOrUpdate(releaseBuild);
            jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        } catch (IOException ioe) {
            jobBuild.setMessage("网络异常");
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
        } catch (JenkinsRuntimeException jre) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage(jre.getMessage());
        } catch (Exception e) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage("UnKnown Error:" + e.getMessage());
        }
        jobBuildService.saveOrUpdate(jobBuild);
    }

    private String getCompileLocation(String compileProductFtpPath) {
        // "ftp://139.199.15.115/" 长度为21
        return "snapshot" + File.separator + compileProductFtpPath.substring(21, compileProductFtpPath.length() - 1);
    }

    @Override
    public void handleCallback(JobBuild jobBuild) {
        ReleaseBuild releaseBuild = releaseService.find(jobBuild.getPluginBuildId());
        releaseBuild.setStatus(jobBuild.getJobStatus());
        releaseService.saveOrUpdate(releaseBuild);
        Map<String, String> newParams = new HashedMap();
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)) {
            newParams.put(JobParameter.PIPELINE_RELEASE_PRODUCT_PATH, releaseBuild.getReleasePath());
            newParams.put(JobParameter.PIPELINE_RELEASE_VERSION, releaseBuild.getVersion());
        }
        jobBuildService.notifiedJobBuildUpdated(jobBuild, newParams);
    }
}