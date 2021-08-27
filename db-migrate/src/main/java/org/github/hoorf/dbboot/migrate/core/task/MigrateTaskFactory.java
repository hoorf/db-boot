package org.github.hoorf.dbboot.migrate.core.task;

import org.github.hoorf.dbboot.migrate.config.GlobalConfig;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.task.inventory.InventoryTask;

public class MigrateTaskFactory {


    public static MigrateTask newTask(MigrateContext context, JobConfig jobConfig, MigratePosition position) {
        if (MigrateTask.MIGRATETASK_INVENTORY.equalsIgnoreCase(jobConfig.getType())) {
            GlobalConfig globalConfig = context.getGlobalConfig();
            InventoryTask inventoryTask = new InventoryTask(new InventoryJobConfig(globalConfig,jobConfig, position));
            inventoryTask.setContext(context);
            return inventoryTask;
        }
        return null;
    }
}
