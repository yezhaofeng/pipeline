package com.jlu.plugin.instance.release.dao;

import java.util.List;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.plugin.instance.release.model.ReleaseBuild;

/**
 * Created by yezhaofeng on 2019/1/20.
 */
public interface IReleaseBuildDao extends IBaseDao<ReleaseBuild>{
    List<ReleaseBuild> get(String module);

    ReleaseBuild getLastest(String module);
}
