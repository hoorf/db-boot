package org.github.hoorf.dbboot.migrate.core.task.inventory;

import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.task.MigrateProgress;

public class InventoryProgress implements MigrateProgress {
    private final MigratePosition<?> position;

    public InventoryProgress(MigratePosition<?> position) {
        this.position = position;
    }
}
