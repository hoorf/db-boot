package org.github.hoorf.dbboot.migrate.dialect.mysql;

import java.util.List;
import org.github.hoorf.dbboot.migrate.core.record.Column;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.AbstractMigrateSQLBuilder;

public class MysqlSQLBuilder extends AbstractMigrateSQLBuilder {
    @Override
    protected String buildInsertOrUpdateSQLInternal(String tableName, List<Column> columnList) {
        StringBuilder labelHolder = new StringBuilder();
        StringBuilder valueHolder = new StringBuilder();
        for (Column column : columnList) {
            labelHolder.append(quote(column.getName())).append(",");
            valueHolder.append("?,");
        }
        labelHolder.setLength(labelHolder.length() - 1);
        valueHolder.setLength(valueHolder.length() - 1);
        return String.format("INSERT INTO %s(%s) VALUES(%s) %s ", quote(tableName), labelHolder, valueHolder, onDuplicateKeyUpdateString(columnList));
    }

    private String onDuplicateKeyUpdateString(List<Column> columnHolders) {
        if (columnHolders == null || columnHolders.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ON DUPLICATE KEY UPDATE ");
        boolean first = true;
        for (Column column : columnHolders) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(quote(column.getName()));
            sb.append("=VALUES(");
            sb.append(quote(column.getName()));
            sb.append(")");
        }

        return sb.toString();
    }

    @Override
    protected String getQuote() {
        return "`";
    }

    @Override
    public String getType() {
        return "mysql";
    }
}
