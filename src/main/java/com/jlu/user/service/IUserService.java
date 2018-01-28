package com.jlu.user.service;

import com.jlu.user.model.GithubUser;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public interface IUserService {

    void saveUser(GithubUser githubUser);

    /**
     * 通过用户名获得密码
     * @param username
     * @return
     */
    GithubUser getUserByName(String username);
}
