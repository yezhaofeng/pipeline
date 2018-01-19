package com.jlu.pipeline.job.dao;

import java.util.List;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */

public interface IJobBuildDao extends IBaseDao<JobBuild> {
    JobBuild getTopJob(Long pipelineBuildId);

    JobBuild getByuUStreamJobBuildId(Long upStreamJobBuildId);

    List<JobBuild> getByPipelineBuildId(Long pipelineBuildId);


}
