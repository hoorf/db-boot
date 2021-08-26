package org.github.hoorf.dbboot.migrate.core.job;

public interface MigrateJob {

    String TYPE_INVENTORY = "inventory";

    void shutdown();
}
