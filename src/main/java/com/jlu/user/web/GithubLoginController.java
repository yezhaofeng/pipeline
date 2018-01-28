package com.jlu.user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jlu.common.cookies.CookiesUtiles;
import com.jlu.common.exception.ForbiddenException;
import com.jlu.common.web.ResponseBean;
import com.jlu.github.service.IGithubDataService;
import com.jlu.user.bean.UserBean;
import com.jlu.user.service.IGithubOAuthService;

/**
 * Created by langshiquan on 18/1/28.
 */
@Controller
@RequestMapping("/github")
public class GithubLoginController {

    private final Logger logger = LoggerFactory.getLogger(GithubLoginController.class);

    @Autowired
    private IGithubDataService githubDataService;

    @Autowired
    private IGithubOAuthService githubOAuthService;

    // 重定向到github的授权页面
    @RequestMapping(value = "/authorization")
    public ModelAndView authorization() {
        return new ModelAndView(new RedirectView(githubOAuthService.getAuthorizationUrl()));
    }

    // github回调pipeline
    @RequestMapping(value = "/login/callback", method = RequestMethod.GET)
    public String callback(String code, String state, Model model) {
        // avoid CSRF
        if (!githubOAuthService.checkState(state)) {
            throw new ForbiddenException("鉴权失败");
        }
        githubOAuthService.handleCallback(code, model);
        return "register";
    }

    // 根据用户注册信息初始化用户
    @RequestMapping(value = "/initUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseBean initUser(HttpServletResponse response, @RequestBody UserBean userBean) {
        githubDataService.initUser(userBean);
        CookiesUtiles.addCookies(response, userBean.getUsername());
        return ResponseBean.TRUE;
    }

    @RequestMapping("/exit")
    @ResponseBody
    public ResponseBean exitLogin(HttpServletResponse response, HttpServletRequest request, String username) {
        CookiesUtiles.deleteCookies(response, request, username);
        return ResponseBean.TRUE;
    }

}
