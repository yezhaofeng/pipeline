package com.jlu.user.dao.impl;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.user.dao.IUserDao;
import com.jlu.user.model.GithubUser;

/**
 * Created by niuwanpeng on 17/3/10.
 *
 * 用户信息管理dao实体类
 */
@Repository
public class UserDaoImpl extends AbstractBaseDao<GithubUser> implements IUserDao {
}
