package org.github.hoorf.dbboot.jdbc.handler;


import java.math.BigDecimal;
import java.sql.*;


public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigDecimal parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setBigDecimal(i, parameter);
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return rs.getBigDecimal(columnName);
    }

    @Override
    public BigDecimal getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return rs.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return cs.getBigDecimal(columnIndex);
    }
}
