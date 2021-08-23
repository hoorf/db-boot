package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class SqlTimestampTypeHandler extends BaseTypeHandler<Timestamp> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Timestamp parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setTimestamp(i, parameter);
    }

    @Override
    public Timestamp getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getTimestamp(columnName);
    }

    @Override
    public Timestamp getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getTimestamp(columnIndex);
    }
}
