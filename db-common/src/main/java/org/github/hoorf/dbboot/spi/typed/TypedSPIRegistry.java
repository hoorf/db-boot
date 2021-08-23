package org.github.hoorf.dbboot.spi.typed;

import com.google.common.base.Preconditions;
import org.github.hoorf.dbboot.spi.DbServiceLoader;

import java.util.Optional;
import java.util.Properties;

public abstract class TypedSPIRegistry {

    public static <T extends TypedSPI> Optional<T> findRegisteredService(final Class<T> typedSPIClass) {
        return DbServiceLoader.newServiceInstances(typedSPIClass).stream().findFirst();
    }

    public static <T extends TypedSPI> Optional<T> findRegisteredService(final Class<T> typedSPIClass, final String type, final Properties properties) {
        Optional<T> optionalService = DbServiceLoader.newServiceInstances(typedSPIClass).stream().filter(each -> each.getType().equalsIgnoreCase(type)).findFirst();
        if (optionalService.isPresent()) {
            T result = optionalService.get();
            copyProperties(result, properties);
            return Optional.of(result);
        }
        return Optional.empty();
    }

    private static <T extends TypedSPI> void copyProperties(T result, Properties properties) {
        if (null != properties) {
            Properties target = new Properties();
            properties.forEach((key, value) -> target.put(key, value));
            result.setProps(target);
        }
    }

    public static <T extends TypedSPI> T getRegisteredService(final Class<T> typedSPIClass, final String type, final Properties properties) {
        Optional<T> result = findRegisteredService(typedSPIClass);
        Preconditions.checkArgument(result.isPresent(), "Not found type %s", type);
        return result.get();
    }

    public static <T extends TypedSPI> T getRegisteredService(final Class<T> typedSPIClass) {
        Optional<T> result = findRegisteredService(typedSPIClass);
        Preconditions.checkArgument(result.isPresent(), "Not found type %s", typedSPIClass.getName());
        return result.get();
    }
}
