package com.jlu.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jlu.user.dao.IUserDao;
import com.jlu.user.model.GithubUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by langshiquan on 18/1/29.
 */
@Component
public class UserLoginHelper {

    private static final ThreadLocal<GithubUser> USER_TL = new ThreadLocal<>();


    private static IUserDao userDao;

    // 使用非静态的setter进行注入
    @Autowired
    public void setUserDao(IUserDao userDao) {
        UserLoginHelper.userDao = userDao;
    }

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
        String pipelineToken = request.getHeader("pipeline-token");
        Object githubUser = null;
        if (StringUtils.isBlank(pipelineToken)) {
            HttpSession session = request.getSession();
            githubUser = session.getAttribute(GithubUser.CURRENT_USER_NAME);
        } else {
            githubUser = userDao.get(pipelineToken);
        }
        if (githubUser instanceof GithubUser) {
            return (GithubUser) githubUser;
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
