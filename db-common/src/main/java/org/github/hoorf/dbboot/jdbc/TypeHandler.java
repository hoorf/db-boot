package org.github.hoorf.dbboot.jdbc;

import java.lang.reflect.Type;
import java.sql.*;

public interface TypeHandler<T> {

    void setParameter(PreparedStatement ps, int i, T parameter, JDBCType jdbcType) throws SQLException;

    T getResult(ResultSet rs, String columnName) throws SQLException;

    T getResult(ResultSet rs, int columnIndex) throws SQLException;

    T getResult(CallableStatement cs, int columnIndex) throws SQLException;

    Type getRawType();

}