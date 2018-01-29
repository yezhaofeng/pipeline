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

import com.jlu.user.service.IUserService;

public class PassportInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (isStaticResource(request)) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // REST传值
            Map attributes = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println(attributes);
        }
        //        HttpSession session = request.getSession();
        //        GithubUser user = (GithubUser) session.getAttribute(GithubUser.CURRENT_USER_NAME);
        //        LoginHelper.register(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (isStaticResource(request)) {
            return;
        }
        ThreadLocalLoginHelper.destory();
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
