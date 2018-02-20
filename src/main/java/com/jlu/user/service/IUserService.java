package com.jlu.user.service;

import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 17/3/10.
 */
public interface IUserService {

    void saveOrUpdateUser(GithubUser githubUser);

    /**
     * 通过用户名获得用户
     * @param username
     * @return
     */
    GithubUser getUserByName(String username);

    Boolean idAdmin(String username);
}
