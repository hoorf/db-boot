package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class ByteTypeHandler extends BaseTypeHandler<Byte> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Byte parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setByte(i, parameter);
    }

    @Override
    public Byte getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getByte(columnName);
    }

    @Override
    public Byte getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getByte(columnIndex);
    }

    @Override
    public Byte getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getByte(columnIndex);
    }
}
