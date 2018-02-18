package com.jlu.user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.user.model.GithubUser;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by langshiquan on 18/1/28.
 */
@ApiIgnore
@RestController
@RequestMapping("/pipeline/user")
public class UserController {

    @PermissionPass
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public GithubUser currentUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return (GithubUser) session.getAttribute(GithubUser.CURRENT_USER_NAME);
    }
}
