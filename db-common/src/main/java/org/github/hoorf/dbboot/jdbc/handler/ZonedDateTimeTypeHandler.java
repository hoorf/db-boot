package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.ZonedDateTime;

public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName, ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex, ZonedDateTime.class);
    }
}
