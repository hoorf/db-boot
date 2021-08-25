package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;

public interface Dumper extends MigrateExecutor {

    void setChannel(MigrateChannel channel);
}
