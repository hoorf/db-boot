package org.github.hoorf.dbboot.migrate.dialect.mysql;

import org.github.hoorf.dbboot.migrate.core.sqlbuilder.AbstractMigrateSQLBuilder;

public class MysqlSQLBuilder extends AbstractMigrateSQLBuilder {
    @Override
    protected String getQuote() {
        return "`";
    }

    @Override
    public String type() {
        return "mysql";
    }
}
