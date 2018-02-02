package com.jlu.common.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

public class PassportInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (Switch.OFF.toString().equals(PipelineConfigReader.getConfigValueByKey("authorization.switch"))) {
            return true;
        }
        if (isStaticResource(request)) {
            return true;
        }
        // 检验是否登陆
        GithubUser githubUser = UserLoginHelper.getLoginUser(request);
        if (githubUser == null) {
            // 重定向到登陆页面
            return false;
        }

        // 检验是否是超级管理员
        if (userService.idAdmin(githubUser.getUsername())) {
            return true;
        }

        // TODO 校验权限
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // REST传值
            Map attributes = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println(attributes);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (isStaticResource(request)) {
            return;
        }
        UserLoginHelper.destory();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }

    protected boolean isStaticResource(HttpServletRequest request) {
        return request.getRequestURL().indexOf("resources/") > 0
                || request.getRequestURL().indexOf("static/") > 0
                || request.getRequestURL().indexOf("css/") > 0
                || request.getRequestURL().indexOf("js/") > 0
                || request.getRequestURL().indexOf("images/") > 0
                || request.getRequestURL().indexOf("html/") > 0;
    }

}
