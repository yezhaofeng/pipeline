package com.jlu.common.interceptor;

import com.jlu.common.utils.PipelineReadConfig;
import com.jlu.user.model.GithubUser;

/**
 * 用来在service中获取当前session中的登录用户，调用方法为User user = LoginHelper.getUser();
 * 如果用户未登录，返回null
 */
public class ThreadLocalLoginHelper {

    protected static String getAuthMethod() {
        return PipelineReadConfig.getConfigValueByKey("user.auth.method");
    }

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

    private static final String USER_AUTH_METHOD_PASSPORT = "passport";

    public static GithubUser getLogin() {
        GithubUser user = null;
        if (USER_AUTH_METHOD_PASSPORT.equalsIgnoreCase(getAuthMethod())) {
            user = getUser();
        } else {
            //            Authentication authentication = SecurityContextHolder.getContext()
            //                    .getAuthentication();
            // 避免类型转换异常
            //            if (authentication != null && authentication.getPrincipal() instanceof User) {
            //                user = (User) authentication.getPrincipal();
            //            }
        }
        return null == user ? new GithubUser() : user;
    }

}

