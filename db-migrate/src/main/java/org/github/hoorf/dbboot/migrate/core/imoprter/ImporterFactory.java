package org.github.hoorf.dbboot.migrate.core.imoprter;

import org.github.hoorf.dbboot.migrate.core.spi.RegisterSpiLoader;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class ImporterFactory extends TypeSpiLoader {


    static {
        RegisterSpiLoader.register(Importer.class);
    }

    public static Importer getInstance(String type) {
        return TypeSpiLoader.findRegisteredService(Importer.class, type, null);
    }
}
