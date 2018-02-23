package com.jlu.common.interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TrackHelper {
    // 父进程的trackId
    public static InheritableThreadLocal<String> trackId = new InheritableThreadLocal<String>();

    // 当前进程的trackId
//    public static ThreadLocal<String> trackId = new ThreadLocal<String>();

    public static void registerTrack(HttpServletRequest request) {
        Map<String, String> restParam =
                (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String queryString = request.getQueryString();
        StringBuilder trackSb = new StringBuilder();
        trackSb.append("[").append(new Date().getTime());
        if (restParam != null && restParam.size() != 0) {
            trackSb.append(",").append(restParam.toString());
        }
        if (StringUtils.isNotBlank(queryString)) {
            trackSb.append(",").append(queryString);
        }
        trackSb.append("]");
        trackId.set(trackSb.toString());
    }
    public static void registerTrack(String trackName) {
        trackId.set(trackName);
    }

    public static void destroyTrack() {
        trackId.remove();
    }

    public static String getTrack() {
        return trackId.get();
    }

//    public static String getTrack() {
//        String parentTrack = parentTrackId.get();
//        String track = trackId.get();
//
//        if (parentTrack == null && track != null) {
//            // "异常情况"
//        }
//        // 一个新生的未跟踪的线程
//        if (parentTrack == null) {
//            track = generateTrack();
//            parentTrackId.set(track);
//            trackId.set(track);
//        } else {
//            // 在一个已经跟踪的线程里，派生的一个还未跟踪子进程
//            if (track == null) {
//                track = parentTrack + "-" + generateTrack();
//                trackId.set(track);
//                parentTrackId.set(track);
//            }
//        }
//        return track;
//    }

    @Deprecated
    private static String generateTrack() {
        try {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stackTraceElement = stackTraceElements[3];
//            System.out.println("class:" + stackTraceElement.getClassName());
//            System.out.println("method:" + stackTraceElement.getMethodName());
            String className = stackTraceElement.getClassName();
            String methodName = stackTraceElement.getMethodName();
            Class cla = Class.forName(className);
            Method[] methods = cla.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] paramClasses = method.getParameterTypes();
                    for (Class paramClass : paramClasses) {
//                        System.out.println(paramClass.getName());
                    }
                }
            }
            return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
