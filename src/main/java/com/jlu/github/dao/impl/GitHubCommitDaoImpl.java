package com.jlu.github.dao.impl;

import com.jlu.branch.bean.BranchType;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.github.dao.IGitHubCommitDao;
import com.jlu.github.model.GitHubCommit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yezhaofeng on 2019/4/25.
 */
@Repository
public class GitHubCommitDaoImpl extends AbstractBaseDao<GitHubCommit> implements IGitHubCommitDao {
    @Override
    public String getModuleById(Long id) {
        GitHubCommit gitHubCommit = findById(id);
        return gitHubCommit == null ? StringUtils.EMPTY : gitHubCommit.getModule();
    }

    @Override
    public List<GitHubCommit> get(String module, BranchType branchType) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("module", module);
        conditionAndSet.put("branchType", branchType);
        List<GitHubCommit> commits = findByProperties(conditionAndSet);
        return commits == null ? new ArrayList<>(0) : commits;
    }
}
