package org.github.hoorf.dbboot.migrate.core;

import org.github.hoorf.dbboot.migrate.core.context.MigrateContextHolder;

public interface MigrateExecutor extends MigrateContextHolder, Runnable {

    void start();

    void stop();
}
