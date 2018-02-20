package com.jlu.branch.service;

import java.util.List;

import com.jlu.branch.model.GithubBranch;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public interface IBranchService {

    void saveBranch(GithubBranch githubBranch);

    void saveBranches(List<GithubBranch> githubBranches);

    List<GithubBranch> getBranchesByModule(String moduleName);

    void updateRemark(Long branchId, String remark);
}
