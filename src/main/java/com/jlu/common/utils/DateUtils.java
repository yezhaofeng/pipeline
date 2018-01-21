package com.jlu.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by niuwanpeng on 17/3/24.
 */
public class DateUtils {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取当前时间格式化输出
     * @return
     */
    public static String getNowTimeFormat() {
        return timeFormat.format(new Date());
    }

    public static String format(Date time) {

        return timeFormat.format(time);
    }

    public static String getNowDateFormat() {
        return dateFormat.format(new Date());
    }
}
