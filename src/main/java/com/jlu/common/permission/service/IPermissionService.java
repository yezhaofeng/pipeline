package com.jlu.common.permission.service;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by langshiquan on 18/2/2.
 */
public interface IPermissionService {
    /**
     * @param paramType  参数类型，例如：pipelineConfId
     * @param paramValue 参数值，例如：178
     *
     * @return
     */
    String getModuleByParamType(String paramType, String paramValue);

    Boolean checkPermission(String module, String username);
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
}
