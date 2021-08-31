package org.github.hoorf.dbboot.migrate.core.spi;

import java.util.Optional;
import java.util.Properties;

public class TypeSpiLoader {


    public static <T extends TypeSpi> T findRegisteredService(final Class<T> typedSPIClass, final String type, final Properties props) {
        Optional<T> serviceInstance = RegisterSpiLoader.newServiceInstances(typedSPIClass).stream().filter(each -> type.equalsIgnoreCase(each.getType())).findFirst();
        if (!serviceInstance.isPresent()) {
            return null;
        }
        T result = serviceInstance.get();
        setProperties(result, props);
        return result;
    }

    private static <T extends TypeSpi> void setProperties(final T service, final Properties props) {
        if (null == props) {
            return;
        }
        Properties newProps = new Properties();
        props.forEach((key, value) -> newProps.setProperty(key.toString(), null == value ? null : value.toString()));
        service.setProps(newProps);
    }


}
