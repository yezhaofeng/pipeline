package com.jlu.branch.service;

import java.util.List;

import com.jlu.branch.model.GithubBranch;
import com.jlu.github.model.Module;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public interface IBranchService {

    /**
     *  保存模块信息
     * @param githubBranch
     */
    void saveBranch(GithubBranch githubBranch);

    /**
     * 批量保存模块信息
     * @param githubBranches
     */
    void saveBranches(List<GithubBranch> githubBranches);

    /**
     * 根据模块数据获得最新的三位版本号＋1
     * @param module
     * @return
     */
    String getLastThreeVersion(Module module);

    /**
     * 获得分支信息
     * @param moduleId
     * @param branchName
     * @return
     */
    GithubBranch getBranchByModule(int moduleId, String branchName);

    /**
     * 获得branchId之前的10条记录
     * @param module
     * @return
     */
    List<GithubBranch> getBranches(Module module, int branchId, int limit);

    /**
     * 获得分支名集合
     * @param username
     * @param module
     * @return
     */
    List<String> getBranchesByModule(String username, String module);
}
