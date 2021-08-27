package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class MigrateSQLBuilderLoader extends TypeSpiLoader {
    static {
        register(MigrateSQLBuilder.class);
    }


    /**
     *
     * @param databaseType
     * @return
     */
    public static MigrateSQLBuilder getInstance(String databaseType) {
        return newInstance(MigrateSQLBuilder.class, databaseType);
    }
}
