package com.jlu.common.permission.service;

import java.util.Set;

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
    public String getModuleByParamType(String paramType, String paramValue);

    public Boolean checkPermission(String module, String username);
    /**
     * @return URL白名单
     */
    public Set<String> getWhiteUrlList();

    /**
     * @return 管理员权限URL
     */
    public Set<String> getAdminUrlList();
}
