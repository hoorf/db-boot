package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JDBCType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, parameter.name());
        } else {
            ps.setObject(i, parameter.name(), jdbcType.getVendorTypeNumber()); // see r3589
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return s == null ? null : Enum.valueOf(type, s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return s == null ? null : Enum.valueOf(type, s);
    }
}