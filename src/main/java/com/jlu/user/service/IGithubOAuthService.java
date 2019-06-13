package com.jlu.user.service;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

/**
 * Created by yezhaofeng on 2019/1/28.
 */
public interface IGithubOAuthService {
    String getAuthorizationUrl();

    Boolean checkState(String state);

    /**
     * @param code    github回调码
     * @param model   注册页面的model
     * @param session HttpSession
     *
     * @return 该用户是否存在
     */
    Boolean handleCallback(String code, Model model, HttpSession session);

    Boolean checkRegisterToken(String registerToken);
}
