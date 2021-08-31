package org.github.hoorf.dbboot.migrate.core.router;

import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.spi.DefaultSpiLoader;
import org.github.hoorf.dbboot.migrate.core.spi.RegisterSpiLoader;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class RouterFactory {

    static {
        RegisterSpiLoader.register(Router.class);
    }


    public static Router getInstance(JobConfig.RouterConfiguration configuration) {
        return configuration == null ?
            DefaultSpiLoader.findService(Router.class) : TypeSpiLoader.findRegisteredService(Router.class, configuration.getType(), configuration.getProps());
    }


}
