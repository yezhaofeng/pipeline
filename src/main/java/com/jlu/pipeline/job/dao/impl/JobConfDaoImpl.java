package com.jlu.pipeline.job.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.pipeline.job.dao.JobConfDao;
import com.jlu.pipeline.job.model.JobConf;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class JobConfDaoImpl extends AbstractBaseDao<JobConf> implements JobConfDao {
    @Override
    public List<JobConf> findByPipelineConfIdAndDeleteStatus(Long pipelineConfId, Boolean deleteStatus) {
        return null;
    }
}
