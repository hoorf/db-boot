package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class DumperFactory extends TypeSpiLoader {

    static {
        register(Dumper.class);
        register(InventoryDumper.class);
    }

    public static Dumper getInstance(String type) {
        return newInstance(Dumper.class, type);
    }

    public static InventoryDumper getInventoryInstance(String type) {
        return newInstance(InventoryDumper.class, type);
    }
}
