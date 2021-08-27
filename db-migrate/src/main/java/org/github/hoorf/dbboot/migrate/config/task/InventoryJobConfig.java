package org.github.hoorf.dbboot.migrate.config.task;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.config.DataSourceConfig;
import org.github.hoorf.dbboot.migrate.config.GlobalConfig;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

@Getter
@Setter
public class InventoryJobConfig extends JobConfig {

    private String dumperType;

    private String importerType;

    private MigratePosition position;


    public InventoryJobConfig(GlobalConfig globalConfig, JobConfig jobConfig, MigratePosition position) {
        Map<String, DataSourceConfig> dataSources = globalConfig.getDataSources();
        this.setSource(jobConfig.getSource());
        this.setTable(jobConfig.getTable());
        this.setTarget(jobConfig.getTarget());
        this.setType(jobConfig.getType());
        this.dumperType = dataSources.get(getSource()).getDatabaseType();
        this.importerType = dataSources.get(getTarget()).getDatabaseType();
        this.position = position;
    }

}
