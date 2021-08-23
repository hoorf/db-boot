package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.LocalDate;

public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName, LocalDate.class);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalDate.class);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex, LocalDate.class);
    }
}