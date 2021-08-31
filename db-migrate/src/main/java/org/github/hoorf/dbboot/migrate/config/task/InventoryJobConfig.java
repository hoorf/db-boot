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
        this.setSource(jobConfig.getSource());
        this.setTarget(jobConfig.getTarget());
        this.setType(jobConfig.getType());
        this.dumperType = getSource().getDumpConfig().getDatabaseType();
        this.importerType = getTarget().getImportConfig().getDatabaseType();
        this.position = position;
    }

}
