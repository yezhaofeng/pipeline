package com.jlu.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

/**
 * Created by niuwanpeng on 17/4/5.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 注册时判断是否为重复用户名
     * @param username
     * @return
     */
    @RequestMapping(value = "/judgeUsernameRepeat", method = RequestMethod.GET)
    @ResponseBody
    public boolean judgeUsernameRepeat(String username) {
        GithubUser githubUser = userService.getUserByName(username);
        return githubUser == null ? false : true;
    }
}
