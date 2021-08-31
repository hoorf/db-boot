package org.github.hoorf.dbboot.migrate.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMigrateExecutor implements MigrateExecutor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean running = false;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    @Override
    public void start() {
        running = true;
        startTime = LocalDateTime.now();
    }

    @Override
    public void stop() {
        running = false;
        endTime = LocalDateTime.now();
    }

    @Override
    public final void run() {
        start();
        run0();
        stop();
        summarize();
    }

    private void summarize() {
        logger.debug("finish run was time {} ms", ChronoUnit.MILLIS.between(startTime, endTime));
    }

    public abstract void run0();

    protected boolean isRunning() {
        return running;
    }
}
