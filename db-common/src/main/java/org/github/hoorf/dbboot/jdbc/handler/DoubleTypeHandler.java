package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class DoubleTypeHandler extends BaseTypeHandler<Double> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Double parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setDouble(i, parameter);
    }

    @Override
    public Double getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getDouble(columnName);
    }

    @Override
    public Double getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getDouble(columnIndex);
    }

    @Override
    public Double getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getDouble(columnIndex);
    }

}
