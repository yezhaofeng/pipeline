package com.jlu.plugin.instance.compile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.jenkins.exception.JenkinsRuntimeException;
import com.jlu.jenkins.service.DefaultJenkinsServer;
import com.jlu.jenkins.service.IJenkinsBuildService;
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
        Map<String, String> compileParam = new HashedMap();

        try {
            Integer buildNumber = jenkinsBuildService
                    .buildJob(DefaultJenkinsServer.ID, COMPILE_JENKINS_JOB_NAME, compileParam,
                            jobBuild);
            // TODO
            String buildPath = "path";
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(File.separator)
                    .append("job").append(File.separator).append(COMPILE_JENKINS_JOB_NAME)
                    .append(File.separator).append(buildNumber)
                    .append(File.separator).append("console");
            compileBuild.setBuildPath(buildPath);
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

}
