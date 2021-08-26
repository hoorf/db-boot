package org.github.hoorf.dbboot.migrate.core.spi;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class TypeSpiLoader {

    private static final Map<Class<?>, Collection<Object>> SERVICES = new ConcurrentHashMap<>();

    protected static void register(Class clazz) {
        SERVICES.put(clazz, Lists.newArrayList(ServiceLoader.load(clazz)));
    }

    protected static Class<?> getClassByType(Class<? extends TypeSpi> clazz, String type) {
        return Optional.ofNullable(SERVICES.get(clazz))
            .flatMap(value -> value.stream()
                .map(each -> (TypeSpi) each).filter(each -> each.type().equals(type)).findFirst())
            .map(value -> value.getClass()).orElse(null);
    }

    protected static <T extends TypeSpi> T newInstance(Class<? extends TypeSpi> clazz, String type) {
        return Optional.ofNullable(getClassByType(clazz, type)).map(value -> {
            try {
                T o = (T) value.newInstance();
                return o;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);
    }


}
