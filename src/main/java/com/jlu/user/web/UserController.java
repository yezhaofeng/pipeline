package com.jlu.user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jlu.common.interceptor.UserLoginHelper;
import com.jlu.common.permission.exception.ForbiddenException;
import com.jlu.common.web.ResponseBean;
import com.jlu.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.user.model.GithubUser;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by langshiquan on 18/1/28.
 */
@ApiIgnore
@RestController
@RequestMapping("/pipeline/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PermissionPass
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public GithubUser currentUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return (GithubUser) session.getAttribute(GithubUser.CURRENT_USER_NAME);
    }

    @RequestMapping(value = "/resetToken")
    public ResponseBean resetToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        GithubUser githubUser = (GithubUser) session.getAttribute(GithubUser.CURRENT_USER_NAME);
        if (githubUser == null) {
            throw new ForbiddenException("此API必须在登录状态访问，不允许通过API访问");
        }
        githubUser.setPipelineToken(UUID.randomUUID().toString());
        userService.saveOrUpdateUser(githubUser);
        session.setAttribute(GithubUser.CURRENT_USER_NAME, githubUser);
        response.sendRedirect("/");
        return ResponseBean.TRUE;
    }
}
