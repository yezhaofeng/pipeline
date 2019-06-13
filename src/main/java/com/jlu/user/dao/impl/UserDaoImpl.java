package com.jlu.user.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jlu.common.db.dao.AbstractBaseDao;
import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.common.utils.CollUtils;
import com.jlu.user.bean.Role;
import com.jlu.user.dao.IUserDao;
import com.jlu.user.model.GithubUser;

/**
 * Created by yezhaofeng on 2019/3/10.
 *
 * 用户信息管理dao实体类
 */
@Repository
public class UserDaoImpl extends AbstractBaseDao<GithubUser> implements IUserDao {
    @Override
    public GithubUser get(String username, Role role) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("username", username);
        conditionAndSet.put("role", role);
        List<GithubUser> users = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(users) ? null : users.get(0);
    }

    @Override
    public GithubUser get(String pipelineToken) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("pipelineToken", pipelineToken);
        List<GithubUser> users = findByProperties(conditionAndSet);
        return CollUtils.isEmpty(users) ? null : users.get(0);
    }
}
