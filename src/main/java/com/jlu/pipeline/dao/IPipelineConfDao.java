package com.jlu.pipeline.dao;

import java.util.List;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.model.PipelineConf;

/**
 * Created by yezhaofeng on 2019/1/14.
 */

public interface IPipelineConfDao extends IBaseDao<PipelineConf> {

    List<PipelineConf> get(String module);

    PipelineConf get(String module, BranchType branchType);

    String getModuleById(Long id);
}
