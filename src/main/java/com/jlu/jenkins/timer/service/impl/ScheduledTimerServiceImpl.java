package com.jlu.jenkins.timer.service.impl;

import com.jlu.common.utils.DateUtils;
import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;
import com.jlu.jenkins.timer.service.ITimerService;
import com.jlu.pipeline.job.model.JobBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/6.
 */
@Service("scheduledTimerServiceImpl")
public class ScheduledTimerServiceImpl implements ITimerService {

    private final Logger logger = LoggerFactory.getLogger(ScheduledTimerServiceImpl.class);

    private ScheduledExecutorService jenkinsJobScheduledExecutor = Executors.newScheduledThreadPool(2);

    private final Vector<JobBuild> vector = new Vector();


    @Override
    public void register(JenkinsBuildTimerTask timerTask, Long delay, Long period) {
        logger.info("register timer task:{} delay:{} period:{}", timerTask, DateUtils.getRealableTime(delay),
                DateUtils.getRealableTime(period));
        timerTask.setVector(vector);
        vector.add(timerTask.getJobBuild());
        jenkinsJobScheduledExecutor.scheduleWithFixedDelay(timerTask, delay, period, TimeUnit.MILLISECONDS);
    }
}
