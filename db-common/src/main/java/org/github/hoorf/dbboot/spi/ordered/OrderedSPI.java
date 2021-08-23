package org.github.hoorf.dbboot.spi.ordered;

public interface OrderedSPI<T> {

    int getOrder();

    Class<T> getTypeClass();
}
