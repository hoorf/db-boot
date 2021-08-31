package org.github.hoorf.dbboot.migrate.core.spi;

import java.util.Properties;

public interface TypeSpi {

    String getType();

    default Properties getProps() {
        return new Properties();
    }

    default void setProps(final Properties props) {

    }
}
