package org.github.hoorf.dbboot.migrate.core.dumper;

import org.github.hoorf.dbboot.migrate.core.spi.TypeSpiLoader;

public class DumperLoader extends TypeSpiLoader {

    static {
        register(Dumper.class);
    }

    public static Dumper getInstance(String type) {
        return newInstance(Dumper.class, type);
    }
}
