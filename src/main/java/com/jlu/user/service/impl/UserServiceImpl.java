package com.jlu.user.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlu.common.db.sqlcondition.ConditionAndSet;
import com.jlu.user.dao.IUserDao;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

/**
 * Created by niuwanpeng on 17/3/10.
 */
@Service
public class UserServiceImpl implements IUserService {
    private static final String ADMIN_USERNAME = "pipeline_admin";
    private static final String ADMIN_PASSWORD = "admin@123";
    private static final String ADMIN_EMAIl = "576506402@qq.com";

    @Autowired
    private IUserDao userDao;

    public void saveUser(GithubUser githubUser) {
        userDao.save(githubUser);
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

    // 插入管理员账户
    @PostConstruct
    public void initAdmin() {
        GithubUser githubUser = getUserByName(ADMIN_USERNAME);
        if (githubUser != null) {
            return;
        }
        GithubUser adminUser = new GithubUser();
        adminUser.setUsername(ADMIN_USERNAME);
        adminUser.setPassword(ADMIN_PASSWORD);
        adminUser.setUserEmail(ADMIN_EMAIl);
        adminUser.setCreateTime(new Date());
        userDao.saveOrUpdate(adminUser);
    }

}
