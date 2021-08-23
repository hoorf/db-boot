package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class ShardingDataSourceConfiguration {

    private String url;
    private String driverClass;
    private String username;
    private String password;
}
