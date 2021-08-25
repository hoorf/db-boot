package org.github.hoorf.dbboot.migrate.core.imoprter;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;

public interface Importer extends MigrateExecutor {

    void setChannel(MigrateChannel channel);
}
