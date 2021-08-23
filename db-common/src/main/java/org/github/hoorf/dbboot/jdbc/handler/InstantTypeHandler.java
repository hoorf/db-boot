package org.github.hoorf.dbboot.jdbc.handler;


import java.sql.*;
import java.time.Instant;

public class InstantTypeHandler extends BaseTypeHandler<Instant> {

    private static Instant getInstant(Timestamp timestamp) {
        if (timestamp != null) {
            return timestamp.toInstant();
        }
        return null;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Instant parameter, JDBCType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter));
    }

    @Override
    public Instant getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return getInstant(timestamp);
    }

    @Override
    public Instant getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        return getInstant(timestamp);
    }

    @Override
    public Instant getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        return getInstant(timestamp);
    }
}