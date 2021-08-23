package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class LongTypeHandler extends BaseTypeHandler<Long> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setLong(i, parameter);
    }

    @Override
    public Long getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getLong(columnName);
    }

    @Override
    public Long getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getLong(columnIndex);
    }

    @Override
    public Long getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getLong(columnIndex);
    }
}
