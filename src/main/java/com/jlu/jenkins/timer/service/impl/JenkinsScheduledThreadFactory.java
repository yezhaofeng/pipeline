package com.jlu.jenkins.timer.service.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2019/3/28.
 */
public class JenkinsScheduledThreadFactory implements ThreadFactory {

    AtomicLong threadNumber = new AtomicLong();

    @Override
    public Thread newThread(Runnable r) {
        // 一定要把Runnable传入进去
        Thread thread = new Thread(r);
        thread.setName("jenkins-scheduled-thread-" + threadNumber.getAndIncrement());
        return thread;
    }
}
