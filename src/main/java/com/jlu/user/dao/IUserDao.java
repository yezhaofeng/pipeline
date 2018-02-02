package com.jlu.user.dao;

import com.jlu.common.db.dao.IBaseDao;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;

/**
 * Created by niuwanpeng on 17/3/10.
 */
public interface IUserDao extends IBaseDao<GithubUser> {

    GithubUser get(String username, Role role);

}
