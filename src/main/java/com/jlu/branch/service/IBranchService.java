package com.jlu.branch.service;

import java.util.List;

import com.jlu.branch.model.GithubBranch;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public interface IBranchService {

    /**
     * 保存模块分支信息
     * @param githubBranch
     */
    void saveBranch(GithubBranch githubBranch);

    /**
     * 批量保存模块分支信息
     * @param githubBranches
     */
    void saveBranches(List<GithubBranch> githubBranches);

    /**
     * 获得分支名集合
     * @param moduleName
     * @return
     */
    List<GithubBranch> getBranchesByModule(String moduleName);

    void updateRemark(Long branchId, String remark);
}
