package com.jlu.plugin.instance.jenkinsjob;

import java.io.File;
import java.io.IOException;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.jlu.plugin.instance.jenkinsjob.dao.IJenkinsJobBuildDao;
import com.jlu.plugin.instance.jenkinsjob.model.JenkinsJobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobExecutor extends AbstractExecutor {

    private Logger logger = LoggerFactory.getLogger(JenkinsJobExecutor.class);
    @Autowired
    private IJenkinsBuildService jenkinsBuildService;

    @Autowired
    private IJenkinsJobBuildDao jenkinsJobDao;

    @Autowired
    private IJobBuildService jobBuildService;

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        JenkinsJobBuild jenkinsJobBuild = jenkinsJobDao.findById(jobBuild.getPluginBuildId());
        if (jobBuild == null) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage("未找到构建");
            jobBuildService.saveOrUpdate(jobBuild);
            return;
        }
        try {
            Integer buildNumber = jenkinsBuildService.buildJob(jenkinsJobBuild.getJenkinsServerId(), jenkinsJobBuild
                    .getJobName(), jobBuild
                    .getInParameterMap(), jobBuild);
            StringBuilder logUrl = new StringBuilder();
            logUrl.append(DefaultJenkinsServer.SERVER_URL).append(File.separator)
                    .append("job").append(File.separator).append(jenkinsJobBuild.getJobName())
                    .append(File.separator).append(buildNumber)
                    .append(File.separator).append("console");
            jenkinsJobBuild.setBuildUrl(logUrl.toString());
            jenkinsJobDao.saveOrUpdate(jenkinsJobBuild);
            jobBuild.setJobStatus(PipelineJobStatus.RUNNING);
        } catch (IOException ioe) {
            jobBuild.setMessage("通讯异常");
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
        } catch (JenkinsRuntimeException jre) {
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage(jre.getMessage());
        } catch (Exception e) {
            logger.error("jenkins job error", e);
            jobBuild.setJobStatus(PipelineJobStatus.FAILED);
            jobBuild.setMessage("UnKnown Error:" + e.getMessage());
        }
        jobBuildService.notifiedJobBuildUpdated(jobBuild, new HashedMap());
    }
}
