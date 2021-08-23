package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Properties;

/**
 * 23
 * dfs
 */
@Getter
@Setter
@ToString
public final class ShardingTableTargetConfiguration {

    private String tableName;

    private TableStrategyConfiguration tableStrategy;

    private String dataSourceName;

    private DatasourceStrategyConfiguration dataSourceStrategy;

    @Getter
    @Setter
    public static final class TableStrategyConfiguration {

        private String type;

        private Properties props;
    }

    @Getter
    @Setter
    public static final class DatasourceStrategyConfiguration {

        private String type;

        private Properties props;
    }

}
