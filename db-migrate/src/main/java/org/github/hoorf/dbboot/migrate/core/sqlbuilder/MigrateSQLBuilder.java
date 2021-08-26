package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.spi.TypeSpi;

public interface MigrateSQLBuilder extends TypeSpi {

    String buildInsertSQL(DataRecord dataRecord);

    String buildSelectPkSQL(String tableName, String pk);
}
