package org.github.hoorf.dbboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsCtrl extends StateCtrl {

    private final String NUMBER = UUID.randomUUID().toString();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger SPEED_COUNTER = new AtomicInteger();

    private final AtomicInteger TOTAL_COUNTER = new AtomicInteger();

    private Timer SPEED_COUNTER_TIMER;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @Override
    protected void fastFail() {
        super.fastFail();
        stopSpeedMonitor();
    }

    @Override
    protected void fastFinish() {
        super.fastFinish();
        stopMonitor();
    }

    protected void countRecord() {
        SPEED_COUNTER.incrementAndGet();
        TOTAL_COUNTER.incrementAndGet();
    }

    protected void countRecord(int incr) {
        TOTAL_COUNTER.addAndGet(incr);
    }

    protected void summary() {
        logger.info("finished : total deal {} rows, use time {} seconds", TOTAL_COUNTER.get(), ChronoUnit.SECONDS.between(startTime, endTime));
    }

    protected void startMonitor() {
        startSpeedMonitor();
        startTimeMonitor();
    }

    private void startTimeMonitor() {
        startTime = LocalDateTime.now();
    }

    protected void stopMonitor() {
        stopSpeedMonitor();
        stopTimeMonitor();
    }

    private void stopTimeMonitor() {
        endTime = LocalDateTime.now();
    }


    private void startSpeedMonitor() {
        SPEED_COUNTER_TIMER = new Timer(true);
        SPEED_COUNTER_TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                if (SPEED_COUNTER.get() > 0) {
                    logger.info("speed was {}r/s", SPEED_COUNTER.get());
                    SPEED_COUNTER.set(0);
                }
            }
        }, 0L, 1000L);
    }

    private void stopSpeedMonitor() {
        if (null != SPEED_COUNTER_TIMER) {
            SPEED_COUNTER_TIMER.cancel();
        }
    }

    public String getId() {
        return NUMBER;
    }
}
