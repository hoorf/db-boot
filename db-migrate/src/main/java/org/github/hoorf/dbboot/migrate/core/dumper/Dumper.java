package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpi;

public interface Dumper extends MigrateExecutor, TypeSpi {

    void setChannel(MigrateChannel channel);

    @Override
    default void setContext(MigrateContext context) {

    }
}
