package com.jlu.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by langshiquan on 18/1/29.
 */
public class AccessLogHelper {
    private static Logger LOG = LoggerFactory.getLogger("accseeLog");
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String HTML = "text/html;charset=UTF-8";
    private static final String JSON = "application/json;charset=UTF-8";

    public static void logAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = LoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        if (POST.equals(method)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream())); //请求体
            String body = IOUtils.toString(reader);
            LOG.info("{} {} {} params:{} body:{} start", username, method, requestUrl, queryString, body);
        } else {
            LOG.info("{} {} {} params:{} start", username, method, requestUrl, queryString);
        }
    }

    public static void logAccess(HttpServletRequest request, HttpServletResponse response, Exception e) {
        String username = LoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        try {
            if (POST.equals(method)) {
                BufferedReader reader = null; //请求体
                reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String body = IOUtils.toString(reader);
                LOG.info("{} {} {} params:{} body:{} end,error:{}", username, method, requestUrl, queryString, body, e);
            } else {
                LOG.info("{} {} {} params:{} end,error:{}", username, method, requestUrl, queryString, e);
            }
        } catch (IOException ioe) {
            LOG.info("{} {} {} params:{} end,error:{},logError:{}", username, method, requestUrl, queryString, e, ioe);
        }
    }
}
