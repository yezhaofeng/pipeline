package com.jlu.github.service;

import com.jlu.branch.bean.BranchType;
import com.jlu.github.model.GitHubCommit;

import java.util.List;

/**
 * Created by langshiquan on 17/4/25.
 */
public interface IGitHubCommitService {

    /**
     * save
     *
     * @param gitHubCommit
     */
    void save(GitHubCommit gitHubCommit);

    GitHubCommit getLastestCommit(String module);

    GitHubCommit get(Long triggerId);

    List<GitHubCommit> get(String module, BranchType branchType);
}
