package org.github.hoorf.dbboot.migrate.core.job;

import java.util.List;
import org.github.hoorf.dbboot.migrate.config.GlobalConfig;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.preparer.MigratePreparer;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTask;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTaskFactory;

public class OneOffMigrateJob implements MigrateJob {

    private GlobalConfig globalConfig;

    private JobConfig jobConfig;

    private MigrateContext context;

    public OneOffMigrateJob(GlobalConfig globalConfig, JobConfig jobConfig) {
        this.globalConfig = globalConfig;
        this.jobConfig = jobConfig;
    }

    public void execute() {
        context = new MigrateContext(globalConfig, jobConfig);
        MigratePreparer preparer = new MigratePreparer();
        List<MigratePosition> positions = preparer.prepare(context);
        for (MigratePosition position : positions) {
            MigrateTask migrateTask = MigrateTaskFactory.newTask(context, jobConfig, position);
           // migrateTask.run();
             context.getExecuteEngine().submit(migrateTask, null);
        }
        context.getExecuteEngine().awaitFinish();
    }


    @Override
    public void shutdown() {
        try {
            context.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
