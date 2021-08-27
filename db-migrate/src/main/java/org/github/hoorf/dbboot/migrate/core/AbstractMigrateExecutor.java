package org.github.hoorf.dbboot.migrate.core;

public abstract class AbstractMigrateExecutor implements MigrateExecutor {

    private volatile boolean running = false;

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        start();
    }

    protected boolean isRunning() {
        return running;
    }
}
