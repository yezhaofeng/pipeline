package com.jlu.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jlu.common.exception.PipelineRuntimeException;

/**
 * Created by yezhaofeng on 2019/1/31.
 */
public class PipelineUtils {
    public static String getFullModule(String owner, String repository) {
        if (StringUtils.isBlank(owner) || StringUtils.isBlank(repository)) {
            throw new PipelineRuntimeException("模块名字不合法");
        }
        return owner + "/" + repository;
    }

    public static String getRepoNameByModule(String module) {
        if (StringUtils.isBlank(module)) {
            throw new PipelineRuntimeException("模块名字不合法");
        }
        String[] elements = module.split("/");
        if (elements.length != 2) {
            throw new PipelineRuntimeException("模块名字不合法");
        }
        return elements[1];
    }


    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> queryParam = new LinkedHashMap<>();
        if (StringUtils.isBlank(queryString)) {
            return queryParam;
        }

        String[] queryArray = queryString.split("&");
        for (String requestParam : queryArray) {
            String[] keyValue = requestParam.split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            queryParam.put(key, value);
        }
        return queryParam;
    }
}
