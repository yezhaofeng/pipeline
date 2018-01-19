package com.jlu.jenkins.timer.service;

import java.util.TimerTask;

/**
 * Created by langshiquan on 18/1/10.
 */
public interface ITimerService {

    void register(TimerTask timerTask, Long delay, Long period);
}
