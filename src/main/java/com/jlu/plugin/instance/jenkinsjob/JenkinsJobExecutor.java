package com.jlu.plugin.instance.jenkinsjob;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.IExecutor;
import com.jlu.plugin.bean.JobBuildContext;

/**
 * Created by langshiquan on 18/1/14.
 */
@Service
public class JenkinsJobExecutor extends IExecutor {

    @Override
    public void execute(JobBuildContext context, JobBuild jobBuild) {
        Map<String, Object> map = new HashedMap();
        map.put("hasd", "usad8");
        jobBuild.setJobStatus(PipelineJobStatus.SUCCESS);
        jobBuildService.notifiedJobBuildFinished(jobBuild, map);
    }
}
