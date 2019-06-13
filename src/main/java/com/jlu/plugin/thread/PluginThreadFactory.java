package com.jlu.plugin.thread;

import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2019/3/28.
 */
public class PluginThreadFactory implements ThreadFactory {

    AtomicLong threadNumber = new AtomicLong();

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("plugin-pool-thread-" + threadNumber.getAndIncrement());
        return thread;
    }
}
