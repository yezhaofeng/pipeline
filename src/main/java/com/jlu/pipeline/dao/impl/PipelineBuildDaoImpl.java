package com.jlu.pipeline.dao.impl;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.pipeline.dao.PipelineBuildDao;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class PipelineBuildDaoImpl extends AbstractBaseDao<PipelineBuild> implements PipelineBuildDao {
    @Override
    public Long getNextBuildNumber(String owner, String module) {
        // TODO: 2018/1/19  
        return null;
    }
}
