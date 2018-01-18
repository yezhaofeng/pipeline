package com.jlu.common.cookies;

import com.jlu.user.model.GithubUser;

/**
 * Created by Administrator on 2018/1/19.
 */
public class LoginHelper {
    public static GithubUser getLoginerUser() {
        return new GithubUser();
    }

    public static String getLoginerUserName() {
        return getLoginerUser().getUsername();
    }

}
