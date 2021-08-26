package org.github.hoorf.dbboot.migrate.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSourceConfig {

    private String name;

    private String databaseType;

    private String url;

    private String driverClass;

    private String username;

    private String password;
}
