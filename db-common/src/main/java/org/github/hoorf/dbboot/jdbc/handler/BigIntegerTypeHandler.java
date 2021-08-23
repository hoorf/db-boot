package org.github.hoorf.dbboot.jdbc.handler;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;


public class BigIntegerTypeHandler extends BaseTypeHandler<BigInteger> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, BigInteger parameter, JDBCType jdbcType) throws SQLException {
        ps.setBigDecimal(i, new BigDecimal(parameter));
    }

    @Override
    public BigInteger getNullableResult(ResultSet rs, String columnName) throws SQLException {
        BigDecimal bigDecimal = rs.getBigDecimal(columnName);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }

    @Override
    public BigInteger getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        BigDecimal bigDecimal = rs.getBigDecimal(columnIndex);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }

    @Override
    public BigInteger getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        BigDecimal bigDecimal = cs.getBigDecimal(columnIndex);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }
}
