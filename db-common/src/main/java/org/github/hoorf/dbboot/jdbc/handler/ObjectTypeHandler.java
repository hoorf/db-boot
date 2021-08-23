package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class ObjectTypeHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getObject(columnName);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getObject(columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getObject(columnIndex);
    }
}
