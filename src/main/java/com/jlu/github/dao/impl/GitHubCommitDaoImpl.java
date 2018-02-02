package com.jlu.github.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.github.dao.IGitHubCommitDao;
import com.jlu.github.model.GitHubCommit;

/**
 * Created by niuwanpeng on 17/4/25.
 */
@Repository
public class GitHubCommitDaoImpl extends AbstractBaseDao<GitHubCommit> implements IGitHubCommitDao {
    @Override
    public String getModuleById(Long id) {
        GitHubCommit gitHubCommit = findById(id);
        return gitHubCommit == null ? StringUtils.EMPTY : gitHubCommit.getModule();
    }
}
