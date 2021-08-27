package org.github.hoorf.dbboot.migrate.core.context.meta;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMeta {

    private List<String> columns;

    private List<String> primaryKeys;

    public TableMeta() {
        this.columns = new ArrayList<>();
        this.primaryKeys = new ArrayList<>();
    }

    public boolean isPrimaryKey(String column) {
        return primaryKeys.contains(column);
    }
}
