
package com.jlu.jenkins.timer.service.impl;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jlu.common.utils.DateUtils;
import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;
import com.jlu.jenkins.timer.service.ITimerService;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by langshiquan on 18/1/10.
 * Timer存在缺陷
 */
@Deprecated
@Service
public class TimerServiceImpl implements ITimerService {

    private final Logger logger = LoggerFactory.getLogger(TimerServiceImpl.class);

    private final Timer timer = new Timer();

    private final Long CLEAR_TASK_PERIOD = 60000L;

    private final Vector<JobBuild> vector = new Vector();

    @Override
    public void register(JenkinsBuildTimerTask timerTask, Long delay, Long period) {
        logger.info("register timer task:{} delay:{} period:{}", timerTask, DateUtils.getRealableTime(delay),
                DateUtils.getRealableTime(period));
        timerTask.setVector(vector);
        vector.add(timerTask.getJobBuild());
        timer.schedule(timerTask, delay, period);
    }

    public Timer getTimer() {
        return timer;
    }

    //    @PostConstruct
    private void clearCanceledTask() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    logger.info("remove {} has canceled timer task ", getTimer().purge());
                } catch (Exception e) {
                    logger.error("clear timer task html.error:{}", e);
                }
            }
        }, new Date(), CLEAR_TASK_PERIOD);
    }

    //    @PreDestroy
    public void stashRuntimeTask() {
        timer.cancel();
        logger.warn("{} task is running", vector.size());
    }

}
