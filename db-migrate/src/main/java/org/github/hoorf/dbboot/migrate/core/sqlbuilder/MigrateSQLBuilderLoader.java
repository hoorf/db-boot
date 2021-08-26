package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class MigrateSQLBuilderLoader extends TypeSpiLoader {
    static {
        register(MigrateSQLBuilder.class);
    }


    public static MigrateSQLBuilder getInstance(String type) {
        return newInstance(MigrateSQLBuilder.class, type);
    }
}
