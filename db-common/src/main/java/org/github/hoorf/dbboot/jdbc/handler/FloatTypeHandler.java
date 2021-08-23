package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class FloatTypeHandler extends BaseTypeHandler<Float> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Float parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setFloat(i, parameter);
    }

    @Override
    public Float getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getFloat(columnName);
    }

    @Override
    public Float getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getFloat(columnIndex);
    }

    @Override
    public Float getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getFloat(columnIndex);
    }
}
