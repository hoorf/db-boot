package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

import static org.github.hoorf.dbboot.migrate.core.spi.RegisterSpiLoader.register;

public class MigrateSQLBuilderLoader {
    static {
        register(MigrateSQLBuilder.class);
    }
    /**
     * @param databaseType
     * @return
     */
    public static MigrateSQLBuilder getInstance(String databaseType) {
        return TypeSpiLoader.findRegisteredService(MigrateSQLBuilder.class, databaseType, null);
    }
}
