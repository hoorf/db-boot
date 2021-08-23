package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class ShortTypeHandler extends BaseTypeHandler<Short> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Short parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setShort(i, parameter);
    }

    @Override
    public Short getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getShort(columnName);
    }

    @Override
    public Short getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getShort(columnIndex);
    }

    @Override
    public Short getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getShort(columnIndex);
    }
}
