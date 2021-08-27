package org.github.hoorf.dbboot.migrate.core.context;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.config.DataSourceConfig;
import org.github.hoorf.dbboot.migrate.config.GlobalConfig;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;
import org.github.hoorf.dbboot.migrate.core.executor.ExecuteEngine;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTask;


public class MigrateContext implements AutoCloseable {
    @Getter
    private ExecuteEngine executeEngine;

    @Getter
    private DataSourceManager dataSourceManager;

    @Getter
    private GlobalConfig globalConfig;

    @Getter
    private JobConfig jobConfig;

    @Setter
    @Getter
    private List<MigrateTask> tasks;

    public MigrateContext(GlobalConfig globalConfig, JobConfig jobConfig) {
        Map<String, DataSourceConfig> dataSources = globalConfig.getDataSources();
        dataSources.entrySet().forEach(each -> each.getValue().setName(each.getKey()));
        dataSourceManager = new DataSourceManager(dataSources.values());
        executeEngine = ExecuteEngine.newCachedThreadPool();
        this.globalConfig = globalConfig;
        this.jobConfig = jobConfig;
    }

    @Override
    public void close() throws Exception {
        executeEngine.close();
        dataSourceManager.close();
    }
}
