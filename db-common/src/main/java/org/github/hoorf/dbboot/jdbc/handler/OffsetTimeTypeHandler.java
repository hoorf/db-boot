package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.OffsetTime;

public class OffsetTimeTypeHandler extends BaseTypeHandler<OffsetTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, OffsetTime parameter, JDBCType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter);
    }

    @Override
    public OffsetTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getObject(columnName, OffsetTime.class);
    }

    @Override
    public OffsetTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getObject(columnIndex, OffsetTime.class);
    }

    @Override
    public OffsetTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getObject(columnIndex, OffsetTime.class);
    }

}
