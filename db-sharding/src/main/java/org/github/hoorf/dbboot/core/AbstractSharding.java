package org.github.hoorf.dbboot.core;

import com.google.common.base.Preconditions;
import org.github.hoorf.dbboot.config.ShardingConfiguration;
import org.github.hoorf.dbboot.config.ShardingDataSourceConfiguration;
import org.github.hoorf.dbboot.config.ShardingGlobalConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableConfiguration;
import org.github.hoorf.dbboot.core.reader.Reader;
import org.github.hoorf.dbboot.core.router.StandardRouter;
import org.github.hoorf.dbboot.core.writer.Writer;
import org.github.hoorf.dbboot.spi.DbServiceLoader;

import java.util.Map;

public class AbstractSharding {

    static {
        DbServiceLoader.register(Reader.class);
        DbServiceLoader.register(Writer.class);
        DbServiceLoader.register(StandardRouter.class);
    }

    private ShardingConfiguration shardingConfiguration;

    public AbstractSharding(ShardingConfiguration shardingConfiguration) {
        this.shardingConfiguration = shardingConfiguration;
    }

    public void validateConfig() {
        Preconditions.checkNotNull(shardingConfiguration);
        Map<String, ShardingDataSourceConfiguration> dataSources = shardingConfiguration
                .getDataSources();
        Preconditions.checkNotNull(dataSources);
        Map<String, ShardingTableConfiguration> tables = shardingConfiguration.getTables();
        Preconditions.checkNotNull(tables);
    }


    public void process() {
        validateConfig();
        Map<String, ShardingDataSourceConfiguration> dataSources = shardingConfiguration.getDataSources();
        Map<String, ShardingTableConfiguration> tableConfigs = shardingConfiguration.getTables();
        ShardingGlobalConfiguration globalConfig = shardingConfiguration.getGlobalConfig();
        for (ShardingTableConfiguration shardingTableConfiguration : tableConfigs.values()) {
            ShardingProcess shardingProcess = new ShardingProcess();
            shardingProcess.config(shardingTableConfiguration, dataSources,globalConfig);
            shardingProcess.start();
        }
    }


}
