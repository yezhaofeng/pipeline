package com.jlu.common.cookies;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by langshiquan on 17/3/16.
 */
public class CookiesUtiles {

    private final static int AUTO_LOGIN_TIMEOUT = 60*60;

    /**
     * 添加cookies
     * @param response
     * @param username
     */
    public static void addCookies(HttpServletResponse response, String username) {
        Cookie usernameCookie = new Cookie("loginUsername", EncryUtil.encrypt(username));
        usernameCookie.setMaxAge(AUTO_LOGIN_TIMEOUT);
        usernameCookie.setPath("/");
        response.addCookie(usernameCookie);
    }

    public static void deleteCookies(HttpServletResponse response, HttpServletRequest request, String username) {
        Cookie usernameCookie = new Cookie("loginUsername", EncryUtil.encrypt(username));
        usernameCookie.setMaxAge(0);
        usernameCookie.setPath("/");
        response.addCookie(usernameCookie);
        request.getSession().setAttribute("loginUser", null);
    }
}
