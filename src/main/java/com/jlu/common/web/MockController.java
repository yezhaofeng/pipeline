package com.jlu.common.web;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jlu.common.permission.annotations.PermissionAdmin;
import com.jlu.common.permission.annotations.PermissionPass;
import com.jlu.common.utils.PipelineConfigReader;
import com.jlu.user.bean.Role;
import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 18/2/3.
 */

@RestController
@RequestMapping("/mock")
public class MockController {

    @PermissionAdmin
    @RequestMapping(value = "/userLogin", method = RequestMethod.GET)
    public ResponseBean userLogin(HttpServletRequest request, HttpServletResponse response, String username,
                                  Role role) {
        GithubUser user = new GithubUser();
        user.setRole(role);
        user.setEmail("576506402@qq.com");
        user.setUsername(username);
        request.getSession().setAttribute(GithubUser.CURRENT_USER_NAME, user);
        return ResponseBean.TRUE;
    }

    @PermissionPass
    @RequestMapping(value = "/login/oauth/authorize", method = RequestMethod.GET)
    public void authorize(HttpServletResponse response, String client_id, String state) throws IOException {
        response.sendRedirect(PipelineConfigReader.getConfigValueByKey("pipeline.home") +
                "/github/login/callback" + "?state=" + state + "&code=" + UUID.randomUUID().toString());
    }

    @PermissionPass
    @RequestMapping(value = "/login/oauth/access_token", method = RequestMethod.POST)
    public String authorize() {
        return "token=123456";
    }

    @PermissionPass
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user() {
        return "{\n"
                + "  \"login\": \"z521598\",\n"
                + "  \"id\": 20750824,\n"
                + "  \"avatar_url\": \"https://avatars1.githubusercontent.com/u/20750824?v=4\",\n"
                + "  \"gravatar_id\": \"\",\n"
                + "  \"url\": \"https://api.github.com/users/z521598\",\n"
                + "  \"html_url\": \"https://github.com/z521598\",\n"
                + "  \"followers_url\": \"https://api.github.com/users/z521598/followers\",\n"
                + "  \"following_url\": \"https://api.github.com/users/z521598/following{/other_user}\",\n"
                + "  \"gists_url\": \"https://api.github.com/users/z521598/gists{/gist_id}\",\n"
                + "  \"starred_url\": \"https://api.github.com/users/z521598/starred{/owner}{/repo}\",\n"
                + "  \"subscriptions_url\": \"https://api.github.com/users/z521598/subscriptions\",\n"
                + "  \"organizations_url\": \"https://api.github.com/users/z521598/orgs\",\n"
                + "  \"repos_url\": \"https://api.github.com/users/z521598/repos\",\n"
                + "  \"events_url\": \"https://api.github.com/users/z521598/events{/privacy}\",\n"
                + "  \"received_events_url\": \"https://api.github.com/users/z521598/received_events\",\n"
                + "  \"type\": \"User\",\n"
                + "  \"site_admin\": false,\n"
                + "  \"name\": null,\n"
                + "  \"company\": null,\n"
                + "  \"blog\": \"\",\n"
                + "  \"location\": null,\n"
                + "  \"email\": null,\n"
                + "  \"hireable\": null,\n"
                + "  \"bio\": null,\n"
                + "  \"public_repos\": 37,\n"
                + "  \"public_gists\": 0,\n"
                + "  \"followers\": 2,\n"
                + "  \"following\": 0,\n"
                + "  \"created_at\": \"2016-07-31T07:40:42Z\",\n"
                + "  \"updated_at\": \"2018-01-15T14:22:33Z\"\n"
                + "}";
    }



}
