package com.jlu.pipeline.dao;

import java.util.List;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.pipeline.model.PipelineBuild;

/**
 * Created by yezhaofeng on 2019/1/14.
 */
public interface IPipelineBuildDao extends IBaseDao<PipelineBuild> {

    Long getNextBuildNumber(String module);

    List<PipelineBuild> get(Long pipelineConfId);

    List<PipelineBuild> get(Long pipelineConfId, int offset, int limit);

    List<PipelineBuild> get(Long pipelineConfId, String branchName);

    List<PipelineBuild> get(Long pipelineConfId, String branchName, int offset, int limit);

    List<PipelineBuild> get(String module, String commitId);

}
