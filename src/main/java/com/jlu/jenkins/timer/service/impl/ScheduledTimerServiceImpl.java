package com.jlu.jenkins.timer.service.impl;

import com.jlu.common.utils.DateUtils;
import com.jlu.jenkins.timer.bean.JenkinsBuildScheduledTask;
import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;
import com.jlu.jenkins.timer.service.IScheduledService;
import com.jlu.jenkins.timer.service.ITimerService;
import com.jlu.pipeline.job.model.JobBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Vector;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2018/3/6.
 */
@Service("scheduledTimerServiceImpl")
public class ScheduledTimerServiceImpl implements IScheduledService {

    private final Logger logger = LoggerFactory.getLogger(ScheduledTimerServiceImpl.class);

    private ScheduledThreadPoolExecutor jenkinsJobScheduledExecutor = new ScheduledThreadPoolExecutor(6);

    {
        jenkinsJobScheduledExecutor.setRemoveOnCancelPolicy(true);
    }

    private ConcurrentHashMap<Long, ScheduledFuture> scheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    public void register(JenkinsBuildScheduledTask timerTask, Long delay, Long period) {
        logger.info("register timer task:{} delay:{} period:{}", timerTask, DateUtils.getRealableTime(delay),
                DateUtils.getRealableTime(period));
        // FIXME maybe throws RejectedExecutionException if the task cannot be scheduled for execution
        ScheduledFuture scheduledFuture = jenkinsJobScheduledExecutor.scheduleWithFixedDelay(timerTask, delay, period, TimeUnit.MILLISECONDS);
        scheduledFutureMap.put(timerTask.getJobBuild().getId(), scheduledFuture);
    }

    @Override
    public void cancel(JobBuild jobBuild) {
        if (jobBuild == null) {
            return;
        }
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(jobBuild.getId());
        if (scheduledFuture != null) {
            // 等待任务完成
            scheduledFuture.cancel(true);
        }
        scheduledFutureMap.remove(jobBuild);
    }

}
