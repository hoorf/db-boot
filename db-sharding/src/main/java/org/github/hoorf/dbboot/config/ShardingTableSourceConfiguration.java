package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public final class ShardingTableSourceConfiguration {

    private String dataSourceName;

    private String tableName;

    private String pk;
}
