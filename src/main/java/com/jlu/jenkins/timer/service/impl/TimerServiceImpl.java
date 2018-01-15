
package com.jlu.jenkins.timer.service.impl;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Service;

import com.jlu.jenkins.timer.service.TimerService;

/**
 * Created by langshiquan on 18/1/10.
 */
@Service
public class TimerServiceImpl implements TimerService {

    private final Timer timer = new Timer();

    @Override
    public void register(TimerTask timerTask, Long delay, Long period) {
        timer.schedule(timerTask, delay, period);
    }

    public Timer getTimer() {
        return timer;
    }
}
