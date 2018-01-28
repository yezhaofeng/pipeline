package com.jlu.github.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jlu.common.exception.ForbiddenException;
import com.jlu.github.service.IGithubOAuthService;

/**
 * Created by langshiquan on 18/1/28.
 */
@Controller
@RequestMapping("/github")
public class GithubLoginController {

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
            throw new ForbiddenException();
        }
        githubOAuthService.handleCallback(code, model);
        return "register";
    }
}
