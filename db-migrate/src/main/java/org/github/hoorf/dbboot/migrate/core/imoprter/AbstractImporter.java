package org.github.hoorf.dbboot.migrate.core.imoprter;

import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;

public abstract class AbstractImporter implements Importer {

    protected MigrateContext context;

    private MigrateChannel channel;

    @Override
    public void setChannel(MigrateChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setContext(MigrateContext context) {
        this.context = context;
    }
}
