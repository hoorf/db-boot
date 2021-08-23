package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.LocalDateTime;

public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName, LocalDateTime.class);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, LocalDateTime.class);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex, LocalDateTime.class);
    }
}