package com.jlu.pipeline.job.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */

public interface IJobBuildDao extends IBaseDao<JobBuild> {
    JobBuild getTopJob(Long pipelineBuildId);
    JobBuild get(Long upStreamJobBuildId);

}
