package com.jlu.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jlu.user.model.GithubUser;

/**
 * Created by langshiquan on 18/1/29.
 */
public class UserLoginHelper {

    private static final ThreadLocal<GithubUser> USER_TL = new ThreadLocal<>();

    /**
     * 在用户验证拦截器中设置session中用户到ThreadLocal中
     *
     * @param user
     */
    public static void register(GithubUser user) {
        USER_TL.set(user);
    }

    /**
     * 获取当前session中的登录用户
     */
    private static GithubUser getUser() {
        return USER_TL.get();
    }

    /**
     * session结束前，用来清除Threadlocal中用户，节约空间
     */
    public static void destory() {
        USER_TL.set(null);
    }
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
        return githubUser == null ? "未知用户" : githubUser.getUsername();
    }

    public static String getLoginUserName() {
        if (getUser() == null) {
            return "未知用户";
        }
        return getUser().getUsername();
    }

    public static GithubUser getLoginUser() {
        return getUser();
    }
}
