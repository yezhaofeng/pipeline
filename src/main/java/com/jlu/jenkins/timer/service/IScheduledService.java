package com.jlu.jenkins.timer.service;

import com.jlu.jenkins.timer.bean.JenkinsBuildScheduledTask;
import com.jlu.pipeline.job.model.JobBuild;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
public interface IScheduledService {

    void register(JenkinsBuildScheduledTask scheduledTask, Long delay, Long period);

    void cancel(JobBuild jobBuild);
}
