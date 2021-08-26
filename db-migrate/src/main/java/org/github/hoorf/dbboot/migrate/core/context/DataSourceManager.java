package org.github.hoorf.dbboot.migrate.core.context;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.github.hoorf.dbboot.migrate.config.DataSourceConfig;

public class DataSourceManager implements AutoCloseable {

    private Map<String, DataSourceWrapper> cacheDataSources = new ConcurrentHashMap<>();

    public DataSourceManager(Collection<DataSourceConfig> configs) {
        for (DataSourceConfig config : configs) {
            cacheDataSources.put(config.getName(), DataSourceFactory.getDataSource(config));
        }
    }

    public DataSourceWrapper getDataSource(String name) {
        return cacheDataSources.get(name);
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
