package com.jlu.common.aop.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jlu.common.exception.PipelineRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jlu.common.interceptor.UserLoginHelper;
import com.jlu.common.utils.DateUtils;

/**
 * Created by langshiquan on 18/1/29.
 */
public class AccessLogHelper {
    private static Logger ACCESS_LOG = LoggerFactory.getLogger("accseeLog");
    private static Logger LOG = LoggerFactory.getLogger(AccessLogHelper.class);

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String HTML = "text/html;charset=UTF-8";
    private static final String JSON = "application/json;charset=UTF-8";

    public static void logAccessIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        String username = UserLoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        ACCESS_LOG.info("{} {} {} params:{} start", username, method, requestUrl, queryString);

    }

    public static void logAccessErrorOut(HttpServletRequest request, HttpServletResponse response, Exception e) {
        String username = UserLoginHelper.getLoginUserName(request);
        String method = request.getMethod();
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString(); // 问号传值
        if(!(e instanceof PipelineRuntimeException)){
            request.setAttribute("error", true);
            LOG.error("{} {} {} params:{} end,error:{}", username, method, requestUrl, queryString, e);
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
        ACCESS_LOG.info("{} {} {} params:{} end,duration:{}({})", username, method, requestUrl, queryString,
                executeTime, DateUtils.getRealableChineseTime(executeTime));

    }
}
