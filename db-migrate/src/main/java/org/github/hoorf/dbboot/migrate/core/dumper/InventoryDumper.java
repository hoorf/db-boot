package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;

public interface InventoryDumper extends Dumper {

    void setConfig(InventoryJobConfig config);
}
