package org.github.hoorf.dbboot.migrate.core.context;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.github.hoorf.dbboot.migrate.config.DataSourceConfig;
import org.github.hoorf.dbboot.migrate.core.context.meta.TableMeta;

public class DataSourceManager implements AutoCloseable {

    private Map<String, DataSourceWrapper> cacheDataSources = new ConcurrentHashMap<>();

    private Map<DataSource, Map<String, TableMeta>> tableMetas = new ConcurrentHashMap<>();

    public DataSourceManager(Collection<DataSourceConfig> configs) {
        for (DataSourceConfig config : configs) {
            cacheDataSources.put(config.getName(), DataSourceFactory.getDataSource(config));
        }
    }

    private Collection<String> loadPrimaryKeys(final Connection connection, final String table) throws SQLException {
        Collection<String> result = new HashSet<>();
        try (ResultSet resultSet = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), connection.getSchema(), table)) {
            while (resultSet.next()) {
                result.add(resultSet.getString("COLUMN_NAME"));
            }
        }
        return result;
    }

    public DataSourceWrapper getDataSource(String name) {
        return cacheDataSources.get(name);
    }

    public TableMeta getTableMeta(String dsName, String tableName) {
        DataSourceWrapper wrapper = cacheDataSources.get(dsName);
        return getTableMeta(wrapper, tableName);
    }

    public TableMeta getTableMeta(DataSourceWrapper dataSource, String tableName) {
        return tableMetas.computeIfAbsent(dataSource, source -> new HashMap<>()).computeIfAbsent(tableName, t -> {
            TableMeta tableMeta = new TableMeta();
            try (Connection connection = dataSource.getConnection()) {
                //default catalog schema
                String catalog = connection.getCatalog();
                String schema = connection.getSchema();
                tableMeta.getPrimaryKeys().addAll(loadPrimaryKeys(connection, tableName));
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet rs = metaData.getColumns(catalog, schema, tableName, "%");
                while (rs.next()) {
                    String columnName = rs.getString("COLUMN_NAME");
                    tableMeta.getColumns().add(columnName);
                }
            } catch (Exception e) {
            }
            return tableMeta;
        });
    }

    @Override
    public void close() throws Exception {
        for (DataSource dataSource : cacheDataSources.values()) {
            if (dataSource instanceof AutoCloseable) {
                ((AutoCloseable) dataSource).close();
            }
        }
    }
}
