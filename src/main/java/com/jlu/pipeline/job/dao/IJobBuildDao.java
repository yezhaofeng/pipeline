package com.jlu.pipeline.job.dao;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
@Repository
public interface IJobBuildDao extends IBaseDao<JobBuild> {
    JobBuild getTopJob(Long pipelineBuildId);
    JobBuild get(Long upStreamJobBuildId);

}
