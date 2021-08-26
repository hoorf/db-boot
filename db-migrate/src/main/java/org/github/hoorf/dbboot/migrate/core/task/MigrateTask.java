package org.github.hoorf.dbboot.migrate.core.task;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;

public interface MigrateTask extends MigrateExecutor {


    String MIGRATETASK_INVENTORY = "inventory";

    String getTaskId();

    MigrateProgress getProgress();
}
