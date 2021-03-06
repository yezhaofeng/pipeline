package com.jlu.common.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jlu.user.dao.IUserDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.aop.annotations.LogExecTime;
import com.jlu.common.permission.exception.ForbiddenException;
import com.jlu.common.permission.service.IPermissionService;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;

public class PassportInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IUserDao userDao;

    @LogExecTime
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 是否开启鉴权
        if (Switch.OFF.toString().equals(PipelineConfigReader.getConfigValueByKey("authorization.switch"))) {
            return true;
        }
        // 静态资源,放行
        if (permissionService.isStaticResource(request)) {
            return true;
        }
        String uri = request.getRequestURI();
        // url白名单,放行
        if (permissionService.getWhiteUrlList().contains(uri)) {
            return true;
        }
        // 获取当前用户
        GithubUser githubUser = null;
        String pipelineToken = request.getHeader("pipeline-token");
        if (StringUtils.isBlank(pipelineToken)) {
            githubUser = UserLoginHelper.getLoginUser(request);
        } else {
            githubUser = userDao.get(pipelineToken);
        }

        // 检验是否登陆
        if (githubUser == null) {
            response.sendRedirect("/login");
            return false;
        }
        String username = githubUser.getUsername();
        // 管理员放行
        if (Role.ADMIN.equals(githubUser.getRole())) {
            return true;
        }
        // 校验具体的资源权限
        if (handler instanceof HandlerMethod) {
            // 普通用户不允许访问管理员url
            if (permissionService.getAdminUrlList().contains(uri)) {
                throw new ForbiddenException("这根香蕉你没权限吃，请联系管理员");
            }
            // REST传值鉴权
            Map<String, String> restParam =
                    (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (restParam != null && restParam.size() != 0) {
                Boolean res = permissionService.checkSourcePermission(restParam, username);
                if (res == false) {
                    throw new ForbiddenException("你没权限吃其他人的香蕉");
                }
                return true;
            } else {
                // 问号传值鉴权
                String queryString = request.getQueryString();
                Map<String, String> queryParam = PipelineUtils.parseQueryString(queryString);
                if (queryParam != null && queryParam.size() != 0) {
                    Boolean res = permissionService.checkSourcePermission(queryParam, username);
                    if (res == false) {
                        throw new ForbiddenException("你没权限吃其他人的香蕉");
                    }
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
    }

}
