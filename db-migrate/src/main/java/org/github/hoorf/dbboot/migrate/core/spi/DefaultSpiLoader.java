package org.github.hoorf.dbboot.migrate.core.spi;

public class DefaultSpiLoader {

    public static <T extends DefaultSpi> T findService(final Class<T> defaultSPIClass) {
        return RegisterSpiLoader.newServiceInstances(defaultSPIClass).stream().filter(each -> each.isDefault()).findFirst().get();
    }
}
