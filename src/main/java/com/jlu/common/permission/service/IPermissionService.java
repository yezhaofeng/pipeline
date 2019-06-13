package com.jlu.common.permission.service;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yezhaofeng on 2019/2/2.
 */
public interface IPermissionService {
    /**
     * @param paramType  参数类型，例如：pipelineConfId
     * @param paramValue 参数值，例如：178
     *
     * @return 是否有权限
     */
    Boolean checkPermissionByParamType(String paramType, String paramValue, String owner);

    /**
     * @return URL白名单
     */
    Set<String> getWhiteUrlList();

    /**
     * @return 管理员权限URL
     */
    Set<String> getAdminUrlList();

    /**
     *
     * @param request
     * @return 是否为静态资源
     */
    Boolean isStaticResource(HttpServletRequest request);

    Boolean checkSourcePermission(Map<String, String> queryParam, String username);
}
