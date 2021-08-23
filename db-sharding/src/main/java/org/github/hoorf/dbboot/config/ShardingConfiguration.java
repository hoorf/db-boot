package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public final class ShardingConfiguration {

    private Map<String, ShardingDataSourceConfiguration> dataSources = new LinkedHashMap<>();
    private Map<String, ShardingTableConfiguration> tables = new LinkedHashMap<>();

    private ShardingGlobalConfiguration globalConfig = new ShardingGlobalConfiguration();

}
