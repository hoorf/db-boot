package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.YearMonth;

public class YearMonthTypeHandler extends BaseTypeHandler<YearMonth> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, YearMonth yearMonth, JDBCType jdbcType) throws SQLException {
        ps.setString(i, yearMonth.toString());
    }

    @Override
    public YearMonth getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return value == null ? null : YearMonth.parse(value);
    }

    @Override
    public YearMonth getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        return value == null ? null : YearMonth.parse(value);
    }

    @Override
    public YearMonth getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        return value == null ? null : YearMonth.parse(value);
    }

}
