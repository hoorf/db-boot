package org.github.hoorf.dbboot.migrate.core.imoprter;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class ImporterLoader extends TypeSpiLoader {


    static {
        register(Importer.class);
    }

    public static Importer getInstance(String type) {
        return newInstance(Importer.class, type);
    }
}
