package com.jlu.common.interceptor;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jlu.common.permission.service.IPermissionService;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.common.utils.PipelineUtils;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IUserService;

public class PassportInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(PassportInterceptor.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IPermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 是否开启鉴权
        if (Switch.OFF.toString().equals(PipelineConfigReader.getConfigValueByKey("authorization.switch"))) {
            return true;
        }
        // 静态资源,放行
        if (isStaticResource(request)) {
            return true;
        }
        String uri = request.getRequestURI();
        // url白名单,放行
        if (permissionService.getWhiteUrlList().contains(uri)) {
            return true;
        }
        // 获取登陆用户
        GithubUser githubUser = UserLoginHelper.getLoginUser(request);

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

        // 普通用户不允许访问管理员url
        if (permissionService.getAdminUrlList().contains(uri)) {
            return false;
        }

        // 校验具体的资源权限
        if (handler instanceof HandlerMethod) {
            // REST传值鉴权
            Map<String, String> restParam =
                    (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println("restParam(REST):" + restParam);
            if (restParam != null && restParam.size() != 0) {
                String owner = restParam.get("owner");
                if (owner != null) {
                    if (owner.equals(username)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    Set<String> keySet = restParam.keySet();
                    Boolean result = true;
                    for (String key : keySet) {
                        String module = permissionService.getModuleByParamType(key, restParam.get(key));
                        Boolean sourcePermission = permissionService.checkPermission(module, username);
                        result = result && sourcePermission;
                    }
                    return result;
                }
            }

            // 问号传值鉴权
            String queryString = request.getQueryString();
            Map<String, String> queryParam = PipelineUtils.parseQueryString(queryString);
            if (queryParam != null && queryParam.size() != 0) {
                Set<String> keySet = queryParam.keySet();
                String requestUrl = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
                System.out.println("requestUrl:" + requestUrl);
                //            HandlerMethod handlerMethod = (HandlerMethod) handler;
                //            MethodParameter[] parameters = handlerMethod.getMethodParameters();
                //            for (int i = 0; parameters != null && parameters.length < i; i++) {
                //                if (!keySet.contains(parameters[i].getParameterName())) {
                //                    return false;
                //                }
                //            }
                Boolean result = true;
                for (String key : keySet) {
                    String module = permissionService.getModuleByParamType(key, restParam.get(key));
                    Boolean sourcePermission = permissionService.checkPermission(module, username);
                    result = result && sourcePermission;
                }
                return result;
            }
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

    // TODO
    protected boolean isStaticResource(HttpServletRequest request) {
        return request.getRequestURL().indexOf("resources/") > 0
                || request.getRequestURL().indexOf("static/") > 0
                || request.getRequestURL().indexOf("css/") > 0
                || request.getRequestURL().indexOf("js/") > 0
                || request.getRequestURL().indexOf("images/") > 0
                || request.getRequestURL().indexOf("html/") > 0;
    }

}
