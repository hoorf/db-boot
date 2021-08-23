package org.github.hoorf.dbboot.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.github.hoorf.dbboot.jdbc.TypeHandler;
import org.github.hoorf.dbboot.jdbc.registry.TypeHandlerRegistry;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
@Slf4j
public final class DbUtils {

    private static String SQL_MIN_MAX_PK = "select min(%s),max(%s) from %s";

    public static DataSource buildDataSource(String jdbcUrl, String driverClass, String userName, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);
        return dataSource;
    }

    @SneakyThrows
    public static <T> T queryFor(Connection connection, String sql) {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        Object object = resultSet.getObject(0, Object.class);
        closeDbResources(resultSet, preparedStatement);
        return (T) object;
    }

    @SneakyThrows
    public static boolean execute(Connection connection, String sql) {
        Statement statement = connection.createStatement();
        boolean success = statement.execute(sql);
        closeDbResources(null, statement);
        return success;
    }

    @SneakyThrows
    public static ResultSet queryForResult(Connection connection, String sql, int fetchSize,
                                           int queryTimeout) {
        connection.setAutoCommit(false);
        Statement stmt = connection
                .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(fetchSize);
        stmt.setQueryTimeout(queryTimeout);
        log.info(stmt.toString());
        return query(stmt, sql);
    }

    public static ResultSet queryForResult(Connection connection, String sql) {
        return queryForResult(connection, sql, 100, 10);
    }

    @SneakyThrows
    public static ResultSet query(Statement stmt, String sql) {
        return stmt.executeQuery(sql);
    }

    @SneakyThrows
    public static void closeResultSet(ResultSet rs) {
        if (null != rs) {
            Statement stmt = rs.getStatement();
            if (null != stmt) {
                stmt.close();
                stmt = null;
            }
            rs.close();
        }
        rs = null;
    }

    @SneakyThrows
    public static List<String> getTableColumns(Connection conn, String tableName) {
        List<String> columns = new ArrayList<>();
        Statement statement = conn.createStatement();
        String queryColumnSql = String.format("select * from %s where 1=2", tableName);
        ResultSet rs = statement.executeQuery(queryColumnSql);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
            columns.add(rsMetaData.getColumnName(i + 1));
        }
        closeDbResources(rs, statement);
        return columns;
    }

    @SneakyThrows
    public static Triple<List<String>, List<Integer>, List<String>> getColumnMetaData(
            Connection connection, String tableName) {
        Triple<List<String>, List<Integer>, List<String>> result = new ImmutableTriple<>(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Statement statement = connection.createStatement();
        String queryColumnSql = String.format("select * from %s where 1=2", tableName);
        ResultSet rs = statement.executeQuery(queryColumnSql);
        ResultSetMetaData rsMetaData = rs.getMetaData();
        for (int i = 0, len = rsMetaData.getColumnCount(); i < len; i++) {
            result.getLeft().add(rsMetaData.getColumnName(i + 1));
            result.getMiddle().add(rsMetaData.getColumnType(i + 1));
            result.getRight().add(rsMetaData.getColumnTypeName(i + 1));
        }
        closeDbResources(rs, statement);
        return result;
    }

    @SneakyThrows
    public Pair<Object, Object> getTableMinMaxPk(Connection conn, String tableName, String pk) {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(String.format(SQL_MIN_MAX_PK, pk, pk, tableName));
        ResultSetMetaData metaData = resultSet.getMetaData();
        int type = metaData.getColumnType(1);
        TypeHandler<?> typeHandler = TypeHandlerRegistry.getTypeHandler(JDBCType.valueOf(type));
        while (resultSet.next()) {
            Object min = typeHandler.getResult(resultSet, 1);
            Object max = typeHandler.getResult(resultSet, 2);
            return new ImmutablePair<>(min, max);
        }
        return null;
    }


    @SneakyThrows
    public static void closeDbResources(ResultSet rs, Statement stmt, Connection conn) {
        if (null != rs) {
            rs.close();
        }
        if (null != stmt) {
            stmt.close();
        }
        if (null != conn) {
            conn.close();
        }
    }

    @SneakyThrows
    public static void closeDbResources(ResultSet rs, Statement stmt) {
        closeDbResources(rs, stmt, null);
    }


    @SneakyThrows
    public static void closeDbResources(Connection conn) {
        closeDbResources(null, null, conn);
    }

    @SneakyThrows
    public static PreparedStatement prepareReadStatement(Connection connection, String sql) {
        connection.setAutoCommit(false);
        PreparedStatement stmt = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(1000);
        stmt.setQueryTimeout(10);
        return stmt;
    }
}
