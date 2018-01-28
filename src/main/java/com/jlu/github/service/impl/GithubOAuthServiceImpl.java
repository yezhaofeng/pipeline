package com.jlu.github.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.HttpClientUtil;
import com.jlu.common.utils.JsonUtils;
import com.jlu.common.utils.PipelineReadConfig;
import com.jlu.github.service.IGithubOAuthService;

/**
 * Created by langshiquan on 18/1/28.
 */
@Service
public class GithubOAuthServiceImpl implements IGithubOAuthService {
    private final Vector<String> stateList = new Vector<>();

    @Override
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        stateList.add(state);
        String authorizationUrl = String.format(PipelineReadConfig.getConfigValueByKey("github.authorize.url"),
                PipelineReadConfig.getConfigValueByKey("github.client.id"), state);
        return authorizationUrl;
    }

    @Override
    public Boolean checkState(String state) {
        if (stateList.contains(state)) {
            stateList.remove(state);
            return true;
        }
        return false;
    }

    @Override
    public void handleCallback(String code) {
        Map<String, String> params = new HashMap();
        params.put("code", code);
        params.put("client_id", PipelineReadConfig.getConfigValueByKey("github.client.id"));
        params.put("client_secret", PipelineReadConfig.getConfigValueByKey("github.client.secret"));
        String token = HttpClientUtil.post(PipelineReadConfig.getConfigValueByKey("github.access.token.url"), params);
        String userInfo = HttpClientUtil
                .get(PipelineReadConfig.getConfigValueByKey("github.user.info.url") + "?" + token, new HashedMap());
        Map<String, String> userInfoMap = JsonUtils.getObjectByJsonString(userInfo, Map.class);
        // TODO: 18/1/28  
    }
}
