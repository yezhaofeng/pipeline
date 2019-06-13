package com.jlu.github.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jlu.branch.bean.BranchType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.db.sqlcondition.DescOrder;
import com.jlu.common.db.sqlcondition.OrderCondition;
import com.jlu.common.utils.CollUtils;
import com.jlu.github.dao.IGitHubCommitDao;
import com.jlu.github.model.GitHubCommit;
import com.jlu.github.service.IGitHubCommitService;

/**
 * Created by yezhaofeng on 2019/4/25.
 */
@Service
public class GitHubCommitServiceImpl implements IGitHubCommitService {

    @Autowired
    private IGitHubCommitDao gitHubCommitDao;

    /**
     * save
     *
     * @param gitHubCommit
     */
    public void save(GitHubCommit gitHubCommit) {
        if (gitHubCommit != null) {
            gitHubCommitDao.save(gitHubCommit);
        }
    }

    @Override
    public GitHubCommit getLastestCommit(String module) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("module", module);
        List<OrderCondition> orders = new ArrayList<OrderCondition>();
        orders.add(new DescOrder("id"));
        List<GitHubCommit> gitHubCommits = gitHubCommitDao.findHeadByProperties(conditionAndSet, orders, 0, 1);
        return CollUtils.isEmpty(gitHubCommits) ? null : gitHubCommits.get(0);
    }

    @Override
    public GitHubCommit get(Long triggerId) {
        return gitHubCommitDao.findById(triggerId);
    }

    @Override
    public List<GitHubCommit> get(String module, BranchType branchType) {
        return gitHubCommitDao.get(module, branchType);
    }

}
