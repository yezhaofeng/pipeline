package com.jlu.user.service;

import com.jlu.user.model.GithubUser;

/**
 * Created by yezhaofeng on 2019/3/10.
         */
public interface IUserService {

    void saveOrUpdateUser(GithubUser githubUser);

    GithubUser getUserByName(String username);

    Boolean idAdmin(String username);

    GithubUser getUserByPipelineToken(String pipelineToken);
}
