package com.jlu.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.permission.service.IPermissionService;

/**
 * Created by langshiquan on 18/2/3.
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    private IPermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o)
            throws Exception {
        if (permissionService.isStaticResource(request)) {
            return true;
        }
        UserLoginHelper.register(UserLoginHelper.getLoginUser(request));
        TrackHelper.registerTrack(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o,
                           ModelAndView modelAndView) throws Exception {
        if (permissionService.isStaticResource(request)) {
            return;
        }
        UserLoginHelper.destory();
        // TrackHelper.destroyTrack();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object o, Exception e) throws Exception {
    }
}
