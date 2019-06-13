package com.jlu.jenkins.timer.service;

import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;

/**
 * Created by yezhaofeng on 2019/1/10.
 */
@Deprecated
public interface ITimerService {

    void register(JenkinsBuildTimerTask timerTask, Long delay, Long period);
}
