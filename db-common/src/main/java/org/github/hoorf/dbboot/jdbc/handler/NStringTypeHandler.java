package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class NStringTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setNString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getNString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getNString(columnIndex);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getNString(columnIndex);
    }

}
