package org.github.hoorf.dbboot.migrate.core;

public interface MigrateExecutor extends Runnable {

    void start();

    void stop();
}
