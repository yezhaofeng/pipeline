package com.jlu.pipeline.job.dao;

import java.util.List;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.job.bean.PipelineJobStatus;
import com.jlu.pipeline.job.model.JobBuild;
import com.jlu.plugin.bean.PluginType;

/**
 * Created by yezhaofeng on 2019/1/14.
 */

public interface IJobBuildDao extends IBaseDao<JobBuild> {
    JobBuild getTopJob(Long pipelineBuildId);

    JobBuild getByUpStreamJobBuildId(Long upStreamJobBuildId);

    List<JobBuild> getByPipelineBuildId(Long pipelineBuildId);

    String getModuleById(Long id);

    JobBuild getLastBuild(Long pipelineBuildId, PluginType pluginType, PipelineJobStatus jobStatus);
}
