package com.jlu.github.service;

import com.jlu.github.model.GitHubCommit;

/**
 * Created by niuwanpeng on 17/4/25.
 */
public interface IGitHubCommitService {

    /**
     * save
     * @param gitHubCommit
     */
    void save(GitHubCommit gitHubCommit);

    GitHubCommit getLastestCommit(String module, String userName);

    GitHubCommit get(Long triggerId);
}
