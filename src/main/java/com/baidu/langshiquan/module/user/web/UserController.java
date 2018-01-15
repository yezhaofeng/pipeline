package com.baidu.langshiquan.module.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.langshiquan.module.user.module.User;
import com.baidu.langshiquan.module.user.service.UserService;

/**
 * Created by langshiquan on 17/10/7.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test/4", method = RequestMethod.GET)
    @ResponseBody
    public User post() {
        User user = new User();
        user.setId(5l);
        user.setName("lsq");
        userService.register(user);
        return user;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User get(@PathVariable Long id) {
        return userService.getUserInfoById(id);
    }
}
