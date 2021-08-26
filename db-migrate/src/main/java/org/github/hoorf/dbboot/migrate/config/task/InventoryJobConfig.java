package org.github.hoorf.dbboot.migrate.config.task;

import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

@Getter
@Setter
public class InventoryJobConfig extends JobConfig {

    private MigratePosition<?> position;


    public InventoryJobConfig() {
    }

    public InventoryJobConfig(JobConfig jobConfig, MigratePosition position) {
        this.setName(jobConfig.getName());
        this.setPk(jobConfig.getPk());
        this.setSource(jobConfig.getSource());
        this.setTable(jobConfig.getTable());
        this.setTarget(jobConfig.getTarget());
        this.setType(jobConfig.getType());
        this.position = position;
    }
}
