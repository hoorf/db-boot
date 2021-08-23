package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class UnknownTypeHandler extends BaseTypeHandler<Object> {

    private static final ObjectTypeHandler OBJECT_TYPE_HANDLER = new ObjectTypeHandler();


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JDBCType jdbcType) throws SQLException {
        throw new RuntimeException("not support for parameter #" + i + " with JDBCType " + jdbcType);
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        throw new RuntimeException("not support for  with columnName " + columnName);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        throw new RuntimeException("not support for  with columnIndex  " + columnIndex);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        throw new RuntimeException("not support for with columnIndex " + columnIndex);
    }
}
