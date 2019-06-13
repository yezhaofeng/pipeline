package com.jlu.branch.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.branch.dao.IBranchDao;
import com.jlu.branch.model.GithubBranch;
import com.jlu.branch.service.IBranchService;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
@Service
public class BranchServiceImpl implements IBranchService {

    @Autowired
    private IBranchDao branchDao;

    /**
     *  保存模块信息
     * @param githubBranch
     */
    @Override
    public void saveBranch(GithubBranch githubBranch) {
        branchDao.save(githubBranch);
    }

    /**
     * 批量保存模块信息
     * @param githubBranches
     */
    @Override
    public void saveBranches(List<GithubBranch> githubBranches) {
        branchDao.saveOrUpdateAll(githubBranches);
    }

    /**
     * 获得分支名集合
     * @param moduleName
     * @return
     */
    @Override
    public List<GithubBranch> getBranchesByModule(String moduleName) {
        return branchDao.find(moduleName);
    }

    @Override
    public void updateRemark(Long branchId, String remark) {
        GithubBranch githubBranch = branchDao.findById(branchId);
        githubBranch.setRemarks(remark);
        branchDao.saveOrUpdate(githubBranch);
    }

}
