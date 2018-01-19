package com.jlu.pipeline.job.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.pipeline.job.dao.IJobConfDao;
import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JobConfDaoImpl extends AbstractBaseDao<JobConf> implements IJobConfDao {

    @Override
    public List<JobConf> findByPipelineConfIdAndDeleteStatus(Long pipelineConfId, Boolean deleteStatus) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineConfId", pipelineConfId);
        conditionAndSet.put("deleteStatus", deleteStatus);
        return findByProperties(conditionAndSet);
    }
}
