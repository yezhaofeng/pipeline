package com.jlu.user.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;

/**
 * Created by yezhaofeng on 2019/3/10.
 */
public interface IUserDao extends IBaseDao<GithubUser> {

    GithubUser get(String username, Role role);

    GithubUser get(String pipelineToken);

}
