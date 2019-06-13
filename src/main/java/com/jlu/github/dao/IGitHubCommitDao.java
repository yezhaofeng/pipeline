package com.jlu.github.dao;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.db.dao.IBaseDao;
import com.jlu.github.model.GitHubCommit;

import java.util.List;

/**
 * Created by yezhaofeng on 2019/4/25.
 */
public interface IGitHubCommitDao extends IBaseDao<GitHubCommit> {
    String getModuleById(Long id);

    List<GitHubCommit> get(String module, BranchType branchType);
}
