package org.github.hoorf.dbboot.migrate.core.sqlbuilder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.migrate.core.record.Column;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;

public abstract class AbstractMigrateSQLBuilder implements MigrateSQLBuilder {

    private static final String INSERT_SQL_CACHE_KEY_PREFIX = "INSERT_";

    private static final String UPDATE_SQL_CACHE_KEY_PREFIX = "UPDATE_";

    private static final String DELETE_SQL_CACHE_KEY_PREFIX = "DELETE_";

    private static final String INSERT_OR_UPDATE_SQL_CACHE_KEY_PREFIX = "INSERT_OR_UPDATE";

    private final ConcurrentMap<String, String> sqlCacheMap = new ConcurrentHashMap<>();


    public String buildInsertSQL(DataRecord dataRecord, final String tableName) {
        String table = tableName == null ? dataRecord.getTableName() : tableName;
        String sqlCacheKey = INSERT_SQL_CACHE_KEY_PREFIX + table;
        return sqlCacheMap.computeIfAbsent(sqlCacheKey, e -> buildInsertSQLInternal(table, dataRecord.getColumns()));
    }

    public String buildInsertOrUpdateSQL(DataRecord dataRecord, final String tableName) {
        String table = tableName == null ? dataRecord.getTableName() : tableName;
        String sqlCacheKey = INSERT_OR_UPDATE_SQL_CACHE_KEY_PREFIX + table;
        return sqlCacheMap.computeIfAbsent(sqlCacheKey, e -> buildInsertOrUpdateSQLInternal(table, dataRecord.getColumns()));
    }

    protected abstract String buildInsertOrUpdateSQLInternal(String tableName, List<Column> columnList);

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

    public String buildSelectPkRangeSQL(String tableName, String pk) {
        return String.format("SELECT max(%s) FROM (SELECT %s FROM %s WHERE %s >= ? LIMIT ?) t", quote(pk), quote(pk), quote(tableName), quote(pk));
    }

    @Override
    public String buildSelectSQL(String tableName, String pk) {
        return String.format("SELECT * FROM %s WHERE %s BETWEEN ? and ? ", quote(tableName), quote(pk));
    }

    public String quote(String str) {
        return new StringBuilder(getQuote()).append(str).append(getQuote()).toString();
    }

    protected abstract String getQuote();

}
