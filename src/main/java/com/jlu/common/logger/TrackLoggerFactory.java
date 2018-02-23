package com.jlu.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2018/2/23.
 */
public class TrackLoggerFactory {
    public static Logger getLogger(Class cla) {
        return new TrackLogger(LoggerFactory.getLogger(cla));
    }

    public static Logger getLogger(String logName) {
        return new TrackLogger(LoggerFactory.getLogger(logName));
    }

}
