package com.jlu.pipeline.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.pipeline.dao.IPipelineBuildDao;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/15.
 */
@Repository
public class PipelineBuildDaoImpl extends AbstractBaseDao<PipelineBuild> implements IPipelineBuildDao {

    @Autowired
    private IPipelineBuildDao IPipelineBuildDao;

    @Override
    public Long getNextBuildNumber(String owner, String module) {
        // TODO: 2018/1/19  
        return null;
    }
}
