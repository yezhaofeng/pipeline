package com.jlu.jenkins.timer.service;

import com.jlu.jenkins.timer.bean.JenkinsBuildTimerTask;

/**
 * Created by langshiquan on 18/1/10.
 */
@Deprecated
public interface ITimerService {

    void register(JenkinsBuildTimerTask timerTask, Long delay, Long period);
}
