package com.jlu.common.logger;

import com.jlu.common.interceptor.TrackHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TrackLogger implements Logger {
    private Logger logger;

    private TrackLogger() {
    }

    public TrackLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        logger.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        logger.trace(wrapLog(s), o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        logger.trace(wrapLog(s), o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        logger.trace(wrapLog(s), objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        logger.trace(wrapLog(s), throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        logger.trace(marker, wrapLog(s));
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        logger.trace(marker, wrapLog(s), o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        logger.trace(marker, wrapLog(s), o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        logger.trace(marker, wrapLog(s), objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        logger.trace(marker, wrapLog(s), throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        logger.debug(wrapLog(s));
    }

    @Override
    public void debug(String s, Object o) {
        logger.debug(wrapLog(s), o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        logger.debug(wrapLog(s), o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        logger.debug(wrapLog(s), objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logger.debug(wrapLog(s), throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        logger.debug(marker, wrapLog(s));
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        logger.debug(marker, wrapLog(s), o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        logger.debug(marker, wrapLog(s), o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        logger.debug(marker, wrapLog(s), objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        logger.debug(marker, wrapLog(s), throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        logger.info(wrapLog(s));
    }

    @Override
    public void info(String s, Object o) {
        logger.info(wrapLog(s), o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        logger.info(wrapLog(s), o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        logger.info(wrapLog(s), objects);
    }

    @Override
    public void info(String s, Throwable throwable) {

        logger.info(wrapLog(s), throwable);
    }


    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        logger.info(marker, wrapLog(s));
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        logger.info(marker, wrapLog(s), o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        logger.info(marker, wrapLog(s), o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        logger.info(marker, wrapLog(s), objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        logger.info(marker, wrapLog(s), throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        logger.warn(wrapLog(s));
    }

    @Override
    public void warn(String s, Object o) {
        logger.warn(wrapLog(s), o);
    }

    @Override
    public void warn(String s, Object... objects) {
        logger.warn(wrapLog(s), objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        logger.warn(wrapLog(s), o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logger.warn(wrapLog(s), throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        logger.warn(marker, wrapLog(s));
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        logger.warn(marker, wrapLog(s), o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        logger.warn(marker, wrapLog(s), o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        logger.warn(marker, wrapLog(s), objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        logger.warn(marker, wrapLog(s), throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        logger.error(wrapLog(s));
    }

    @Override
    public void error(String s, Object o) {
        logger.error(wrapLog(s), o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        logger.error(wrapLog(s), o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        logger.error(wrapLog(s), objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(wrapLog(s), throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String s) {
        logger.error(marker, wrapLog(s));
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        logger.error(marker, wrapLog(s), o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        logger.error(marker, wrapLog(s), o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        logger.error(marker, wrapLog(s), objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        logger.error(marker, wrapLog(s), throwable);
    }

    private String wrapLog(String s) {
        String track = TrackHelper.getTrack();
        if (track == null) {
            return s;
        }
        return s = track + s + " ";
    }
}
