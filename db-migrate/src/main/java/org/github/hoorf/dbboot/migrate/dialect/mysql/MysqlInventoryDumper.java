package org.github.hoorf.dbboot.migrate.dialect.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.github.hoorf.dbboot.migrate.core.dumper.AbstractInventoryDumper;

public class MysqlInventoryDumper extends AbstractInventoryDumper {
    @Override
    protected PreparedStatement createStatement(Connection connection, String sql) throws SQLException {
        PreparedStatement result = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        result.setFetchSize(Integer.MIN_VALUE);
        return result;
    }

    @Override
    public String getType() {
        return "mysql";
    }
}
