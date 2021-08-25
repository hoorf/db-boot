package org.github.hoorf.dbboot.migrate.core.task;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;

public interface MigrateTask extends MigrateExecutor {

    String getTaskId();

    MigrateProgress getProgress();
}
