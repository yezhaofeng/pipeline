package com.jlu.plugin.instance.compile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.DateUtils;
import com.jlu.jenkins.exception.JenkinsRuntimeException;
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
 * Created by langshiquan on 18/1/20.
 */
@Service
public class CompileExecutor extends AbstractExecutor {

    private final static String COMPILE_JENKINS_JOB_NAME = "compile";
    private final static String JOB_BUILD_ID = "JOB_BUILD_ID";
    private final static String RANDOM_UUID = "RANDOM_UUID";
    private final static String CURRENT_DATA = "CURRENT_DATA";
    private final static String OWNER = "OWNER";
    private final static String FTP_SERVER_URL = "ftp://139.199.15.115/";

    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private ICompileBuildDao compileBuildDao;

    @Autowired
    private IJobBuildService jobBuildService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        CompileBuild compileBuild = compileBuildDao.findById(jobBuild.getPluginBuildId());
        if (jobBuild == null) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage("未找到构建");
            jobBuildService.saveOrUpdate(jobBuild);
            return;
        }
        Map<String, String> compileParam = jobBuild.getInParameterMap();
        compileParam.put(JOB_BUILD_ID, String.valueOf(jobBuild.getId()));
        compileParam.put(RANDOM_UUID, UUID.randomUUID().toString());
        compileParam.put(CURRENT_DATA, DateUtils.getNowDateFormat());
        compileParam.put(OWNER, context.getPipelineBuild().getOwner());
        try {
            Integer buildNumber = jenkinsBuildService
                    .buildJob(DefaultJenkinsServer.ID, COMPILE_JENKINS_JOB_NAME, compileParam,
                            jobBuild);
            StringBuilder buildPath = new StringBuilder();
            buildPath.append(FTP_SERVER_URL).append(compileParam.get(JobParameter.PIPELINE_MODULE))
                    .append(File.separator).append(context.getPipelineBuild().getOwner())
                    .append(File.separator).append(compileParam.get(JobParameter.PIPELINE_COMMIT_ID))
                    .append(File.separator).append(compileParam.get(CURRENT_DATA))
                    .append(File.separator).append(compileParam.get(JOB_BUILD_ID))
                    .append(File.separator).append(compileParam.get(RANDOM_UUID));
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(File.separator)
                    .append("job").append(File.separator).append(COMPILE_JENKINS_JOB_NAME)
                    .append(File.separator).append(buildNumber)
                    .append(File.separator).append("console");
            compileBuild.setBuildPath(buildPath.toString());
            compileBuild.setLogUrl(logUrl.toString());
            compileBuildDao.saveOrUpdate(compileBuild);
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

    @Override
    public void handleCallback(JobBuild jobBuild) {
        CompileBuild compileBuild = compileBuildDao.findById(jobBuild.getPluginBuildId());
        Map<String, String> newParams = new HashedMap();
        if (jobBuild.getJobStatus().equals(PipelineJobStatus.SUCCESS)) {
            newParams.put(JobParameter.PIPELINE_COMPILE_PRODUCT_PATH, compileBuild.getBuildPath());
        }
        jobBuildService.notifiedJobBuildUpdated(jobBuild, newParams);
    }
}