package org.github.hoorf.dbboot.migrate.core.context;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.github.hoorf.dbboot.migrate.config.DataSourceConfig;

public class DataSourceFactory {


    public static DataSourceWrapper getDataSource(DataSourceConfig config) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setDriverClassName(config.getDriverClass());
        return new DataSourceWrapper(config.getDatabaseType(), dataSource);
    }
}
