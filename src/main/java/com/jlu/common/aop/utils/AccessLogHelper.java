package com.jlu.common.aop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlu.common.utils.DateUtils;
import com.jlu.common.interceptor.UserLoginHelper;

/**
 * Created by langshiquan on 18/1/29.
 */
public class AccessLogHelper {
    private static Logger LOG = LoggerFactory.getLogger("accseeLog");
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String HTML = "text/html;charset=UTF-8";
    private static final String JSON = "application/json;charset=UTF-8";

    public static void logAccessIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        String username = UserLoginHelper.getLoginUserName(request);
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

    public static void logAccessErrorOut(HttpServletRequest request, HttpServletResponse response, Exception e) {
        String username = UserLoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        request.setAttribute("error", true);
        try {
            if (POST.equals(method)) {
                BufferedReader reader = null; //请求体
                reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String body = IOUtils.toString(reader);
                LOG.info("{} {} {} params:{} body:{} end,error:{}", username, method, requestUrl,
                        queryString, body, e);
            } else {
                LOG.info("{} {} {} params:{} end,error:{}", username, method, requestUrl, queryString, e);
            }
        } catch (IOException ioe) {
            LOG.info("{} {} {} params:{} end,error:{},logError:{}", username, method, requestUrl, queryString, e, ioe);
        }
    }

    public static void logAccessOut(HttpServletRequest request, HttpServletResponse response) {
        Object errorObj = request.getAttribute("error");
        // 若发生异常此处不打印日志
        if (errorObj != null && (Boolean) errorObj == true) {
            return;
        }
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        String username = UserLoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        try {
            if (POST.equals(method)) {
                BufferedReader reader = null; //请求体
                reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String body = IOUtils.toString(reader);
                LOG.info("{} {} {} params:{} body:{} end,duration:{}({})", username, method, requestUrl, queryString,
                        body, executeTime, DateUtils.getRealableChineseTime(executeTime));
            } else {
                LOG.info("{} {} {} params:{} end,duration:{}({})", username, method, requestUrl, queryString,
                        executeTime, DateUtils.getRealableChineseTime(executeTime));
            }
        } catch (IOException ioe) {
            LOG.info("{} {} {} params:{} end,duration:{}({}) logError:{}", username, method, requestUrl, queryString,
                    executeTime, DateUtils.getRealableChineseTime(executeTime), ioe);
        }
    }
}
