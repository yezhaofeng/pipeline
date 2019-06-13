package com.jlu.plugin.instance.compile;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.jlu.common.exception.PipelineRuntimeException;
import com.jlu.jenkins.model.JenkinsConf;
import com.jlu.jenkins.service.IJenkinsServerService;
import com.jlu.jenkins.timer.service.IScheduledService;
import com.jlu.plugin.bean.PluginType;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;
import com.offbytwo.jenkins.JenkinsServer;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.DateUtils;
import com.jlu.jenkins.exception.JenkinsException;
import com.jlu.jenkins.service.DefaultJenkinsServer;
import com.jlu.jenkins.service.IJenkinsBuildService;
import com.jlu.pipeline.job.bean.JobParameter;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.pipeline.job.service.IJobBuildService;
import com.jlu.plugin.AbstractExecutor;
import com.jlu.plugin.bean.JobBuildContext;
import com.jlu.plugin.instance.compile.dao.ICompileBuildDao;
import com.jlu.plugin.instance.compile.model.CompileBuild;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
@Service
public class CompileExecutor extends AbstractExecutor {
    private final static String SEPARATOR = "/";
    private final static String COMPILE_JENKINS_JOB_NAME = "compile";
    private final static String JOB_BUILD_ID = "JOB_BUILD_ID";
    private final static String RANDOM_UUID = "RANDOM_UUID";
    private final static String CURRENT_DATA = "CURRENT_DATA";
    private final static String OWNER = "OWNER";
    private final static String PIPELINE_PARAMETER = "PIPELINE_PARAMETER";
    /*private final static String FTP_SERVER_URL = "ftp://139.199.15.115/";*/
    private final static String FTP_SERVER_URL = "ftp://139.196.97.69/";

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private ICompileBuildDao compileBuildDao;

    @Autowired
    private IJobBuildService jobBuildService;

    @Autowired
    private IJenkinsServerService jenkinsServerService;

    @Autowired
    private IScheduledService scheduledService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        Map<String, String> compileParam = jobBuild.getInParameterMap();
        CompileBuild compileBuild = compileBuildDao.findById(jobBuild.getPluginBuildId());
        String module = compileParam.get(JobParameter.PIPELINE_MODULE_NAME);
        String commitId = compileParam.get(JobParameter.PIPELINE_COMMIT_ID);
        Long pipelineBuildId = Long.parseLong(compileParam.get(JobParameter.PIPELINE_BUILD_ID));
        // 复用编译产出
        if (pipelineBuildId != null && compileBuild.getMulti()) {
            JobBuild lastSuccBuild = jobBuildService.getLastSuccBuild(module, commitId, PluginType.COMPILE, pipelineBuildId);
            // 7天清空编译产出
            if (lastSuccBuild != null && DateUtils.addDays(lastSuccBuild.getTriggerTime(), 7).after(new Date())) {
                CompileBuild lastSuccCompile = compileBuildDao.findById(lastSuccBuild.getPluginBuildId());
                compileBuild.setBuildPath(lastSuccCompile.getBuildPath());
                compileBuild.setLogUrl(lastSuccCompile.getLogUrl());
                compileBuildDao.update(compileBuild);
                jobBuild.setStartTime(new Date());
                jobBuild.setName(jobBuild.getName() + "(复用)");
                jobBuild.setJobStatus(PipelineJobStatus.SUCCESS);
                handleCallback(jobBuild);
                return;
            }
        }

        compileParam.put(JOB_BUILD_ID, String.valueOf(jobBuild.getId()));
        compileParam.put(RANDOM_UUID, UUID.randomUUID().toString());
        compileParam.put(CURRENT_DATA, DateUtils.getNowDateFormat());
        compileParam.put(PIPELINE_PARAMETER, jobBuild.getInParams());
        try {
            Integer buildNumber = jenkinsBuildService
                    .buildJob(DefaultJenkinsServer.ID, COMPILE_JENKINS_JOB_NAME, compileParam,
                            jobBuild);
            StringBuilder buildPath = new StringBuilder();
            buildPath.append(FTP_SERVER_URL).append("snapshot")
                    .append(SEPARATOR).append(module)
                    .append(SEPARATOR).append(commitId)
                    .append(SEPARATOR).append(compileParam.get(CURRENT_DATA))
                    .append(SEPARATOR).append(compileParam.get(JOB_BUILD_ID))
                    .append(SEPARATOR).append(compileParam.get(RANDOM_UUID));
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(SEPARATOR)
                    .append("job").append(SEPARATOR).append(COMPILE_JENKINS_JOB_NAME)
                    .append(SEPARATOR).append(buildNumber)
                    .append(SEPARATOR).append("console");
            compileBuild.setBuildPath(buildPath.toString());
            compileBuild.setLogUrl(logUrl.toString());
            compileBuild.setBuildNumber(buildNumber);
            compileBuildDao.saveOrUpdate(compileBuild);
            jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        } catch (IOException ioe) {
            jobBuild.setMessage("网络异常");
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            throw new PipelineRuntimeException(ioe.getMessage());
        } catch (JenkinsException jre) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage(jre.getMessage());
            throw new PipelineRuntimeException(jre.getMessage());
        } catch (Exception e) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage("UnKnown Error:" + e.getMessage());
            throw new PipelineRuntimeException(e.getMessage());
        }finally {
            jobBuildService.saveOrUpdate(jobBuild);
        }

    }

    @Override
    public void handleCallback(JobBuild jobBuild) {
        CompileBuild compileBuild = compileBuildDao.findById(jobBuild.getPluginBuildId());
        Map<String, String> newParams = new HashedMap();
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)) {
            newParams.put(JobParameter.PIPELINE_COMPILE_PRODUCT_PATH, compileBuild.getBuildPath());
        }
        jobBuildService.notifiedJobBuildFinished(jobBuild, newParams);
    }

    @Override
    public void cancel(JobBuild jobBuild) {
        Long pluginBuildId = jobBuild.getPluginBuildId();
        CompileBuild compileBuild = compileBuildDao.findById(pluginBuildId);
        String serverUrl = DefaultJenkinsServer.SERVER_URL;
        Integer buildNumber = compileBuild.getBuildNumber();
        String jobName = COMPILE_JENKINS_JOB_NAME;
        JenkinsServer jenkinsServer = jenkinsServerService.getJenkinsServer(serverUrl, DefaultJenkinsServer.MASTER_USERNAME, DefaultJenkinsServer.MASTER_PASSWORD);
        try {
            jenkinsServerService.cancel(jenkinsServer, jobName, buildNumber);
        } catch (IOException e) {
            throw new PipelineRuntimeException("网络异常");
        }
        scheduledService.cancel(jobBuild);
        notifiedJobBuildCanceled(jobBuild);
    }
}
