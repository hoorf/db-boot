package org.github.hoorf.dbboot.migrate.core.router;

import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.spi.DefaultSpi;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpi;

public interface Router extends TypeSpi, DefaultSpi {


    String route(DataRecord dataRecord);

    @Override
    default boolean isDefault() {
        return false;
    }
}
