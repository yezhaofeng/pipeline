package com.jlu.github.service;

import net.sf.json.JSONObject;

/**
 * Created by yezhaofeng on 2019/4/19.
 */
public interface IGitHubHookService {

    /**
     * 解析hook信息，触发流水线
     * @param hookMessage
     */
    void dealHookMessage(JSONObject hookMessage);
}
