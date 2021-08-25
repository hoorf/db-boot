package org.github.hoorf.dbboot.migrate.core.spi;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class TypeSpiLoader {


    private static final Map<Class<?>, Collection<Object>> SERVICES = new ConcurrentHashMap<>();


    public static void register(Class clazz) {
        SERVICES.put(clazz, Lists.newArrayList(ServiceLoader.load(clazz)));
    }


    public static Class<?> getClassByType(Class<? extends TypeSpi> clazz, String type) {
        return Optional.ofNullable(SERVICES.get(clazz))
            .flatMap(value -> value.stream()
                .map(each -> (TypeSpi) each).filter(each -> each.type().equals(type)).findFirst()).orElse(null).getClass();
    }
}
