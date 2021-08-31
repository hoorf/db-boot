package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpi;

public interface MigrateSQLBuilder extends TypeSpi {

    String buildInsertSQL(DataRecord dataRecord,String tableName);

    String buildSelectPkRangeSQL(String tableName, String pk);

    public String buildInsertOrUpdateSQL(DataRecord dataRecord,String tableName);

    String buildSelectSQL(String tableName, String pk);
}
