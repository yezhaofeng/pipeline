package com.jlu.pipeline.job.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JobBuildDaoImpl extends AbstractBaseDao<JobBuild> implements IJobBuildDao {

    @Override
    public JobBuild getTopJob(Long pipelineBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        conditionAndSet.put("upStreamJobBuildId", 0L);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }

    @Override
    public JobBuild getByuUStreamJobBuildId(Long upStreamJobBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("upStreamJobBuildId", upStreamJobBuildId);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }

    @Override
    public List<JobBuild> getByPipelineBuildId(Long pipelineBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jobBuilds) ? new ArrayList<>(0) : jobBuilds;
    }
}
