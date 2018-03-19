package com.jlu.plugin.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/3/18.
 */
@Component
public class PluginThreadService {
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor pluginExecutor = new ThreadPoolExecutor(6, 10, 10, TimeUnit.MINUTES, workQueue);

    public void execute(Runnable runnable) {
        pluginExecutor.execute(runnable);
    }
}