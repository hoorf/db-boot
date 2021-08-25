package org.github.hoorf.dbboot.migrate.core.record;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

@Getter
@Setter
public class DataRecord extends Record {

    private String tableName;

    private String opType;

    private List<String> primaryKeyName = new LinkedList<>();

    private List<Object> primaryKeyValue = new LinkedList<>();

    private List<Column> columns;

    public DataRecord(MigratePosition<?> position) {
        super(position);
        columns = new ArrayList<>();
    }

    public void addColumn(Column data) {
        columns.add(data);
        if (data.isPrimaryKey()) {
            primaryKeyName.add(data.getName());
            primaryKeyValue.add(data.getValue());
        }
    }

    public Column getColumn(int index) {
        return columns.get(index);
    }

    public String getIdentityKey() {
        return Joiner.on("@").join(tableName, primaryKeyValue);
    }
}
