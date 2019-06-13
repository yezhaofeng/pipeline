package com.jlu.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlu.common.utils.CollUtils;
import com.jlu.common.utils.HttpClientUtil;
import com.jlu.common.utils.JsonUtils;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.github.bean.GithubRepoBean;
import com.jlu.user.model.GithubUser;
import com.jlu.user.service.IGithubOAuthService;
import com.jlu.user.service.IUserService;

/**
 * Created by yezhaofeng on 2019/1/28.
 */
@Service
public class GithubOAuthServiceImpl implements IGithubOAuthService {
    private Logger logger = LoggerFactory.getLogger(IGithubOAuthService.class);
    private final Vector<String> stateList = new Vector<>();
    private final Vector<String> registerTokenList = new Vector<>();
    @Autowired
    private IUserService userService;

    @Override
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        stateList.add(state);
        logger.info("state:{} add in cache,current cache size:{}", state, stateList.size());
        String authorizationUrl = String.format(PipelineConfigReader.getConfigValueByKey("github.authorize.url"),
                PipelineConfigReader.getConfigValueByKey("github.client.id"), state);
        return authorizationUrl;
    }

    @Override
    public Boolean checkState(String state) {
        if (stateList.contains(state)) {
            stateList.remove(state);
            logger.info("state:{} has callback,current cache size:{}", state, stateList.size());
            return true;
        }
        return false;
    }

    @Override
    public Boolean handleCallback(String code, Model model, HttpSession session) {
        Map<String, String> params = new HashMap();
        params.put("code", code);
        params.put("client_id", PipelineConfigReader.getConfigValueByKey("github.client.id"));
        params.put("client_secret", PipelineConfigReader.getConfigValueByKey("github.client.secret"));
        String token = HttpClientUtil.post(PipelineConfigReader.getConfigValueByKey("github.access.token.url"), params);
        String userInfo = HttpClientUtil
                .get(PipelineConfigReader.getConfigValueByKey("github.user.info.url") + "?" + token, null);
        Map<String, String> userInfoMap = JsonUtils.getObjectByJsonString(userInfo, Map.class);
        String username = userInfoMap.get("login");
        GithubUser githubUser = userService.getUserByName(username);
        // 第一次登陆的用户
        if (githubUser == null) {
            String reposUrl = userInfoMap.get("repos_url");
            String avatarUrl = userInfoMap.get("avatar_url");
            String githubHome = userInfoMap.get("html_url");
            String email = userInfoMap.get("email");
            String registerToken = UUID.randomUUID().toString();
            String result = HttpClientUtil.get(reposUrl, null);
            List<GithubRepoBean> repoList = new Gson().fromJson(result, new TypeToken<List<GithubRepoBean>>() {
            }.getType());
            List<String> repoNameList = CollUtils.toList(repoList, GithubRepoBean.NAME_GETTER);
            model.addAttribute("username", username);
            model.addAttribute("repoNames", repoNameList);
            model.addAttribute("avatarUrl", avatarUrl);
            model.addAttribute("githubHome", githubHome);
            model.addAttribute("email", email);
            model.addAttribute("registerToken", registerToken);
            registerTokenList.add(registerToken);
            return false;
        } else {
            session.setAttribute(GithubUser.CURRENT_USER_NAME, githubUser);
            return true;
        }
    }

    @Override
    public Boolean checkRegisterToken(String registerToken) {
        if (registerTokenList.contains(registerToken)) {
            registerTokenList.remove(registerToken);
            logger.info("state:{} has callback,current cache size:{}", registerToken, registerTokenList.size());
            return true;
        }
        return false;
    }
}
