package com.jlu.plugin.thread;

import com.jlu.common.exception.PipelineRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/3/18.
 */

/**
 * 这里不能使用@Component,如果是@Component则也会在SpringMVC容器中创建一个对象,导致依赖注入的时候，会有2个对象(一个来自Spring容器)注入到不同的Service，
 * 导致此Service的数据不一致，从而导致BUG
 */
@Service
public class PluginThreadService {
    @Autowired
    private ApplicationContext applicationContext;
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
    private static final int CORE_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 10;
    private ThreadPoolExecutor pluginExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 10, TimeUnit.MINUTES, workQueue);
    private volatile ConcurrentHashMap<Long, Thread> jobBuildThreadMap = new ConcurrentHashMap<>();

    public void execute(PluginTask pluginTask) {
        try {
            pluginExecutor.execute(pluginTask);
        } catch (RejectedExecutionException re) {
            throw new PipelineRuntimeException("系统繁忙,请稍后");
        }
    }

    public void register(Long jobBuildId, Thread thread) {

        jobBuildThreadMap.put(jobBuildId, thread);
        System.out.println(jobBuildThreadMap);
    }

    public void destroy(Long jobBuildId) {
        jobBuildThreadMap.remove(jobBuildId);
    }


    public Thread getJobBuildThread(Long jobBuildId) {
        // FIXME: 2018/3/20 如何保证对象只在一个容器里?
        System.out.println(applicationContext.getId());
        System.out.println(System.identityHashCode(jobBuildThreadMap));
        System.out.println(System.identityHashCode(this));
        System.out.println(jobBuildThreadMap);
        return jobBuildThreadMap.get(jobBuildId);
    }

    public ConcurrentHashMap<Long, Thread> getJobBuildThreadMap() {
        return jobBuildThreadMap;
    }
}