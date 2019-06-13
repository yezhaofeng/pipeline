package com.jlu.branch.dao;

import java.util.List;

import com.jlu.branch.model.GithubBranch;
import com.jlu.common.db.dao.IBaseDao;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
public interface IBranchDao extends IBaseDao<GithubBranch> {
    List<GithubBranch> find(String moduleName);

    String getModuleById(Long id);
}
