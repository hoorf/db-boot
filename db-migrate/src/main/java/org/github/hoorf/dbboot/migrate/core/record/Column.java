package org.github.hoorf.dbboot.migrate.core.record;

import lombok.Getter;

@Getter
public class Column {
    private final String name;

    private final Object value;

    private final boolean primaryKey;

    public Column(String name, Object value, boolean primaryKey) {
        this.name = name;
        this.value = value;
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "Column{" +
            "name='" + name + '\'' +
            ", value=" + value +
            ", primaryKey=" + primaryKey +
            '}';
    }
}

