package org.github.hoorf.dbboot.spi.typed;

import java.util.Properties;

public interface TypedSPI {

    String getType();

    default Properties getProps() {
        return new Properties();
    }

    default void setProps(final Properties props) {

    }
}
