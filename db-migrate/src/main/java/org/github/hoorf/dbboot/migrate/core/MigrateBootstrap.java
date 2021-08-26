package org.github.hoorf.dbboot.migrate.core;

import org.github.hoorf.dbboot.migrate.config.GlobalConfig;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.config.MigrateConfig;
import org.github.hoorf.dbboot.migrate.core.job.MigrateJob;
import org.github.hoorf.dbboot.migrate.core.job.OneOffMigrateJob;

public class MigrateBootstrap {

    public void start(MigrateConfig config) {
        GlobalConfig globalConfig = config.getGlobalConfig();
        config.getJobs().entrySet().forEach(each -> each.getValue().setName(each.getKey()));
        for (JobConfig job : config.getJobs().values()) {
            if(MigrateJob.TYPE_INVENTORY.equals(job.getType())){
                new OneOffMigrateJob(globalConfig,job).execute();
            }
        }
    }
}
