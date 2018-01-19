package com.jlu.pipeline.job.dao.impl;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.pipeline.job.dao.IJobBuildDao;
import com.jlu.pipeline.job.model.JobBuild;

import java.util.List;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JobBuildDaoImpl extends AbstractBaseDao<JobBuild> implements IJobBuildDao {
    @Override
    public JobBuild get(Long pipelineBuildId, Long upStreamJobBuildId) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineBuildId", pipelineBuildId);
        conditionAndSet.put("upStreamJobBuildId", upStreamJobBuildId);
        List<JobBuild> jobBuilds = findByProperties(conditionAndSet);
        return CollectionUtils.isEmpty(jobBuilds) ? null : jobBuilds.get(0);
    }
}
