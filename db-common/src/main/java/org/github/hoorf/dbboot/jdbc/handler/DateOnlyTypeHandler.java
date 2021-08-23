package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.util.Date;


public class DateOnlyTypeHandler extends BaseTypeHandler<Date> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setDate(i, new java.sql.Date((parameter.getTime())));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        java.sql.Date sqlDate = rs.getDate(columnName);
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        java.sql.Date sqlDate = rs.getDate(columnIndex);
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        java.sql.Date sqlDate = cs.getDate(columnIndex);
        if (sqlDate != null) {
            return new Date(sqlDate.getTime());
        }
        return null;
    }

}
