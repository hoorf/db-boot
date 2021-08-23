package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.Year;

public class YearTypeHandler extends BaseTypeHandler<Year> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Year year, JDBCType jdbcType) throws SQLException {
        ps.setInt(i, year.getValue());
    }

    @Override
    public Year getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int year = rs.getInt(columnName);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }

    @Override
    public Year getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int year = rs.getInt(columnIndex);
        return year == 0 && rs.wasNull() ? null : Year.of(year);
    }

    @Override
    public Year getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int year = cs.getInt(columnIndex);
        return year == 0 && cs.wasNull() ? null : Year.of(year);
    }

}
