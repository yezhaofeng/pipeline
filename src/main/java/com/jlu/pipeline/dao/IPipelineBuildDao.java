package com.jlu.pipeline.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by langshiquan on 18/1/14.
 */
public interface IPipelineBuildDao extends IBaseDao<PipelineBuild> {

    Long getNextBuildNumber(String owner, String module);
}
