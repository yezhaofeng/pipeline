package com.jlu.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 18/2/3.
 */
@PermissionAdmin
@RestController
@RequestMapping("/mock")
public class MockController {

    @RequestMapping(value = "/userLogin", method = RequestMethod.GET)
    public ResponseBean userLogin(HttpServletRequest request, HttpServletResponse response, String username,
                                  Role role) {
        GithubUser user = new GithubUser();
        user.setRole(role);
        user.setEmail("576506402@qq.com");
        user.setUsername(username);
        request.getSession().setAttribute(GithubUser.CURRENT_USER_NAME, user);
        return ResponseBean.TRUE;
    }

}
