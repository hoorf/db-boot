package org.github.hoorf.dbboot.migrate.dialect.mysql;

import org.github.hoorf.dbboot.migrate.core.imoprter.AbstractImporter;

public class MysqlImporter extends AbstractImporter {
    @Override
    public String type() {
        return "mysql";
    }
}
