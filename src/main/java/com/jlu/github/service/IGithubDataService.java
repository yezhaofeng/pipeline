package com.jlu.github.service;

import com.jlu.user.bean.UserBean;
import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 17/3/24.
 *
 * 同 github 交互服务
 */
public interface IGithubDataService {

    /**
     * 根据用户注册信息初始化用户
     * @param userBean
     * @return
     */
    GithubUser initUser(UserBean userBean);

    /**
     * 为代码仓库创建hook
     * @param username
     * @param repo
     * @param githubPassword
     * @return
     */
    void creatHooks(String username, String repo, String githubPassword);

    /**
     * 增加新的模块
     * @param username
     * @param repository
     * @return
     */
    void addModule(String username, String repository);
}
