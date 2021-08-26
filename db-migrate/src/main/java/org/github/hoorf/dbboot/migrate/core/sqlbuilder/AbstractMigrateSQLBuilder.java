package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.github.hoorf.dbboot.migrate.core.record.Column;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;

public abstract class AbstractMigrateSQLBuilder implements MigrateSQLBuilder {

    private static final String INSERT_SQL_CACHE_KEY_PREFIX = "INSERT_";

    private static final String UPDATE_SQL_CACHE_KEY_PREFIX = "UPDATE_";

    private static final String DELETE_SQL_CACHE_KEY_PREFIX = "DELETE_";

    private final ConcurrentMap<String, String> sqlCacheMap = new ConcurrentHashMap<>();


    public String buildInsertSQL(DataRecord dataRecord) {
        String sqlCacheKey = INSERT_SQL_CACHE_KEY_PREFIX + dataRecord.getTableName();
        return sqlCacheMap.computeIfAbsent(sqlCacheKey, e -> buildInsertSQLInternal(dataRecord.getTableName(), dataRecord.getColumns()));
    }

    private String buildInsertSQLInternal(String tableName, List<Column> columnList) {
        StringBuilder labelHolder = new StringBuilder();
        StringBuilder valueHolder = new StringBuilder();
        for (Column column : columnList) {
            labelHolder.append(quote(column.getName())).append(",");
            valueHolder.append("?,");
        }
        labelHolder.setLength(labelHolder.length() - 1);
        valueHolder.setLength(valueHolder.length() - 1);
        return String.format("INSERT INTO %s(%s) VALUES(%s)", quote(tableName), labelHolder, valueHolder);
    }

    public String buildSelectPkSQL(String tableName, String pk) {
        return String.format("SELECT max(%s) FROM (SELECT %s FROM %s WHERE %s >= ? LIMIT ?) t", quote(pk), quote(pk), quote(tableName),quote(pk));
    }

    public String quote(String str) {
        return new StringBuilder(getQuote()).append(str).append(getQuote()).toString();
    }

    protected abstract String getQuote();

}
