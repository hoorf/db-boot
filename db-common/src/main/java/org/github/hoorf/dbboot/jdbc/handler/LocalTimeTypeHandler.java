package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.LocalTime;


public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName, LocalTime.class);
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalTime.class);
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex, LocalTime.class);
    }
}