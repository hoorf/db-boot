package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.core.spi.RegisterSpiLoader;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class DumperFactory {

    static {
        RegisterSpiLoader.register(Dumper.class);
        RegisterSpiLoader.register(InventoryDumper.class);
    }

    public static Dumper getInstance(String type) {
        return TypeSpiLoader.findRegisteredService(Dumper.class, type, null);
    }

    public static InventoryDumper getInventoryInstance(String type) {
        return TypeSpiLoader.findRegisteredService(InventoryDumper.class, type, null);
    }
}
