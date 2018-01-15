package com.baidu.langshiquan.module.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baidu.langshiquan.module.user.dao.UserDao;
import com.baidu.langshiquan.module.user.module.User;

/**
 * Created by langshiquan on 17/10/7.
 */
@Transactional(noRollbackFor = ArithmeticException.class)
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean register(User user) {

        userDao.save(user);
        return true;
    }

    public User getUserInfoById(Long id) {
        return userDao.findById(id);
    }

}
