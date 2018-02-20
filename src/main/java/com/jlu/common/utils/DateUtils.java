package com.jlu.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by niuwanpeng on 17/3/24.
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    // SimpleDateFormat线程不安全
    //    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String TIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    //    private static final SimpleDateFormat dateFormat = new Sim/leDateFormat("yyyy-MM-dd");
    private static final String DATE_FORMAT_STR = "yyyy-MM-dd";
    private static final String ZERO_SECONDS_READAVLE = "<0ms";
    private static final String ZERO_SECONDS_CHINESE_READAVLE = "小于1毫秒";
    private static final String MSEC_READABLE = "ms";
    private static final String MSEC_CHINESE_READABLE = "毫秒";
    private static final List<DateRealableBean> readableList = new LinkedList<>();

    static {
        DateRealableBean dayReadable = new DateRealableBean();
        dayReadable.setMillis(MILLIS_PER_DAY);
        dayReadable.setChineseReabale("天");
        dayReadable.setReabale("d");
        readableList.add(dayReadable);

        DateRealableBean hourReadable = new DateRealableBean();
        hourReadable.setMillis(MILLIS_PER_HOUR);
        hourReadable.setChineseReabale("时");
        hourReadable.setReabale("h");
        readableList.add(hourReadable);

        DateRealableBean minuteReadable = new DateRealableBean();
        minuteReadable.setMillis(MILLIS_PER_MINUTE);
        minuteReadable.setChineseReabale("分");
        minuteReadable.setReabale("m");
        readableList.add(minuteReadable);

        DateRealableBean secondsReadable = new DateRealableBean();
        secondsReadable.setMillis(MILLIS_PER_SECOND);
        secondsReadable.setChineseReabale("秒");
        secondsReadable.setReabale("s");

        readableList.add(secondsReadable);
    }

    /**
     * 获取当前时间格式化输出
     *
     * @return
     */
    public static String getNowTimeFormat() {
        return new SimpleDateFormat(TIME_FORMAT_STR).format(new Date());
    }

    public static String format(Date time) {

        return new SimpleDateFormat(TIME_FORMAT_STR).format(time);
    }

    /**
     * 获取当前日期格式化输出
     *
     * @return
     */
    public static String getNowDateFormat() {
        return new SimpleDateFormat(DATE_FORMAT_STR).format(new Date());
    }

    /**
     * 将毫秒数转换成易读的数字
     * e.g.:1d7h12m9s
     *
     * @param time
     * @return
     */
    public static String getRealableTime(Long time) {
        if (time == 0L) {
            return ZERO_SECONDS_READAVLE;
        }
        if (time < MILLIS_PER_SECOND) {
            return time + MSEC_READABLE;
        }
        StringBuilder timeSb = new StringBuilder();
        for (int i = 0; i < readableList.size(); i++) {
            DateRealableBean dateRealableBean = readableList.get(i);
            Long num = time / dateRealableBean.getMillis();
            time = time % dateRealableBean.getMillis();
            if (num != 0L) {
                timeSb.append(num).append(dateRealableBean.getReabale());
            }
        }
        return timeSb.toString();
    }

    /**
     * 将毫秒数转换成易读的中文数字
     * e.g.:1d7h12m9s
     *
     * @param time
     * @return
     */
    public static String getRealableChineseTime(Long time) {
        if (time == 0L) {
            return ZERO_SECONDS_CHINESE_READAVLE;
        }
        if (time < MILLIS_PER_SECOND) {
            return time + MSEC_CHINESE_READABLE;
        }
        StringBuilder timeSb = new StringBuilder();
        for (int i = 0; i < readableList.size(); i++) {
            DateRealableBean dateRealableBean = readableList.get(i);
            Long num = time / dateRealableBean.getMillis();
            time = time % dateRealableBean.getMillis();
            if (num != 0L) {
                timeSb.append(num).append(dateRealableBean.getChineseReabale());
            }
        }
        return timeSb.toString();
    }

    private static class DateRealableBean {
        private Long millis;
        private String reabale;
        private String chineseReabale;

        public Long getMillis() {
            return millis;
        }

        public void setMillis(Long millis) {
            this.millis = millis;
        }

        public String getReabale() {
            return reabale;
        }

        public void setReabale(String reabale) {
            this.reabale = reabale;
        }

        public String getChineseReabale() {
            return chineseReabale;
        }

        public void setChineseReabale(String chineseReabale) {
            this.chineseReabale = chineseReabale;
        }
    }

}
