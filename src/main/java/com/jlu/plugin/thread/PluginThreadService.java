package com.jlu.plugin.thread;

import com.jlu.common.exception.PipelineRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>(WORK_QUEUE_SIZE);
    private static final int CORE_POOL_SIZE = 6;
    private static final int MAX_POOL_SIZE = 12;
    private static final int WORK_QUEUE_SIZE = 50;
    private ThreadPoolExecutor pluginExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, 10, TimeUnit.MINUTES, workQueue);
    private volatile ConcurrentHashMap<Long, Thread> jobBuildThreadMap = new ConcurrentHashMap<>();

    public void execute(PluginTask pluginTask) throws RejectedExecutionException {
        pluginExecutor.execute(pluginTask);
    }

    public void register(Long jobBuildId, Thread thread) {
        jobBuildThreadMap.put(jobBuildId, thread);
    }

    public void destroy(Long jobBuildId) {
        jobBuildThreadMap.remove(jobBuildId);
    }


    public Thread getJobBuildThread(Long jobBuildId) {
        // FIXME: 2018/3/20 如何保证对象只在一个容器里?
        return jobBuildThreadMap.get(jobBuildId);
    }

    /**
     *
     * @param pluginTask
     * @return
     * true 有此Task且remove成功
     * false 无此Task
     */
    public boolean removeInQueue(PluginTask pluginTask) {
        return workQueue.remove(pluginTask);
    }

    public ConcurrentHashMap<Long, Thread> getJobBuildThreadMap() {
        return jobBuildThreadMap;
    }
}