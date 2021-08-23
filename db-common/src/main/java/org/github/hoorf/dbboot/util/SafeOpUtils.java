package org.github.hoorf.dbboot.util;

import java.util.function.Function;

public class SafeOpUtils {

    public static <P, V> V op(P value, Function<P, V> function, V defaultValue) {
        return null != value ? function.apply(value) : defaultValue;
    }
}
