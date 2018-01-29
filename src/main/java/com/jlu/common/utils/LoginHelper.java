package com.jlu.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 18/1/29.
 */
public class LoginHelper {
    public static GithubUser getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object object = session.getAttribute(GithubUser.CURRENT_USER_NAME);
        if (object instanceof GithubUser) {
            return (GithubUser) object;
        }
        return null;
    }

    public static String getLoginUserName(HttpServletRequest request) {
        GithubUser githubUser = getLoginUser(request);
        return githubUser == null ? StringUtils.EMPTY : githubUser.getUsername();
    }

}
