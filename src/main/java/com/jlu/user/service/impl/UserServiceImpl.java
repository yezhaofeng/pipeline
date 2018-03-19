package com.jlu.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.user.bean.Role;
import com.jlu.user.dao.IUserDao;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

/**
 * Created by langshiquan on 17/3/10.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    public void saveOrUpdateUser(GithubUser githubUser) {
        userDao.saveOrUpdate(githubUser);
    }

    /**
     * 通过用户民获得密码
     * @param username
     * @return
     */
    public GithubUser getUserByName(String username) {
        ConditionAndSet conditionAndSet = new ConditionAndSet();
        conditionAndSet.put("username", username);
        List<GithubUser> users = userDao.findByProperties(conditionAndSet);
        if (users != null && users.size() != 0) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public Boolean idAdmin(String username) {
        GithubUser githubUser = userDao.get(username, Role.ADMIN);
        if (githubUser == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public GithubUser getUserByPipelineToken(String pipelineToken) {
        return userDao.get(pipelineToken);
    }

}
