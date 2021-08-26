package org.github.hoorf.dbboot.migrate.core.imoprter;

import org.github.hoorf.dbboot.migrate.core.MigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpi;

public interface Importer extends MigrateExecutor, TypeSpi {

    void setChannel(MigrateChannel channel);

    @Override
    default void setContext(MigrateContext context) {

    }
}
