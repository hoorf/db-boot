package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;


public class CharacterTypeHandler extends BaseTypeHandler<Character> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Character parameter, JDBCType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public Character getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        if (columnValue != null) {
            return columnValue.charAt(0);
        } else {
            return null;
        }
    }

    @Override
    public Character getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (columnValue != null) {
            return columnValue.charAt(0);
        } else {
            return null;
        }
    }

    @Override
    public Character getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (columnValue != null) {
            return columnValue.charAt(0);
        } else {
            return null;
        }
    }
}
