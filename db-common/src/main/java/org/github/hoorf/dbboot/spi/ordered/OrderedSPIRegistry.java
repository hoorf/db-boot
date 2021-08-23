package org.github.hoorf.dbboot.spi.ordered;

import com.google.common.base.Preconditions;
import org.github.hoorf.dbboot.spi.DbServiceLoader;

import java.util.*;

/**
 * 获取带顺序的SPI处理
 */
public abstract class OrderedSPIRegistry {


    public static <T extends OrderedSPI<?>> Map<Class<?>, T> getRegisteredServicesByClass(final Collection<Class<?>> types, final Class<T> orderedSPIClass) {
        Collection<T> registeredServices = getRegisteredServices(orderedSPIClass);
        Map<Class<?>, T> result = new LinkedHashMap<>(registeredServices.size(), 1);
        for (T each : registeredServices) {
            types.stream().filter(type -> each.getTypeClass() == type).forEach(type -> result.put(type, each));
        }
        return result;
    }

    public static <T extends OrderedSPI<?>> Collection<T> getRegisteredServices(final Class<T> orderedSPIClass) {
        return getRegisteredServices(orderedSPIClass, Comparator.naturalOrder());
    }

    private static <T extends OrderedSPI<?>> Collection<T> getRegisteredServices(final Class<T> orderedSPIClass, final Comparator<Integer> comparator) {
        Map<Integer, T> result = new TreeMap<>(comparator);
        for (T each : DbServiceLoader.getServiceInstances(orderedSPIClass)) {
            Preconditions.checkArgument(!result.containsKey(each.getOrder()), "Found same order %s with %s and %s", each.getOrder(), result.get(each.getOrder()), each);
            result.put(each.getOrder(), each);
        }
        return result.values();
    }


}
