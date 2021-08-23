package org.github.hoorf.dbboot.jdbc.handler;


import org.github.hoorf.dbboot.jdbc.TypeHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;

public abstract class BaseTypeHandler<T> implements TypeHandler<T> {
    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JDBCType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new IllegalArgumentException("JDBC requires that the JDBCType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.getVendorTypeNumber());
            } catch (SQLException e) {
                throw new RuntimeException("Error setting null for parameter #" + i + " with JDBCType " + jdbcType + " . " +
                        "Try setting a different JDBCType for this parameter or a different JDBCTypeForNull configuration property. " +
                        "Cause: " + e, e);
            }
        } else {
            try {
                setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception e) {
                throw new RuntimeException("Error setting non null for parameter #" + i + " with JDBCType " + jdbcType + " . " +
                        "Try setting a different JDBCType for this parameter or a different configuration property. " +
                        "Cause: " + e, e);
            }
        }
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        T result;
        try {
            result = getNullableResult(rs, columnName);
        } catch (Exception e) {
            throw new RuntimeException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        T result;
        try {
            result = getNullableResult(rs, columnIndex);
        } catch (Exception e) {
            throw new RuntimeException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        T result;
        try {
            result = getNullableResult(cs, columnIndex);
        } catch (Exception e) {
            throw new RuntimeException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
        if (cs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public Type getRawType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type rawType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) rawType).getRawType();
        }
        return rawType;
    }

    public abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JDBCType JDBCType) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

    public abstract T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException;
}
