package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;

public class ArrayTypeHandler extends BaseTypeHandler<Object> {

    public ArrayTypeHandler() {
        super();
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType) throws SQLException {
        ps.setArray(i, (Array) parameter);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        return array == null ? null : array.getArray();
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return array == null ? null : array.getArray();
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return array == null ? null : array.getArray();
    }

}
