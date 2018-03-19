package com.jlu.plugin.thread;

import com.jlu.common.exception.PipelineRuntimeException;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/3/18.
 */
@Component
public class PluginThreadService {
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
    private static final int CORE_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 10;
    private ThreadPoolExecutor pluginExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 10, TimeUnit.MINUTES, workQueue);

    public void execute(PluginTask pluginTask) {
        try {
            pluginExecutor.execute(pluginTask);
        } catch (RejectedExecutionException re) {
            throw new PipelineRuntimeException("系统繁忙,请稍后");
        }
    }

}