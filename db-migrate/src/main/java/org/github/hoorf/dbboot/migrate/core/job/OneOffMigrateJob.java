package org.github.hoorf.dbboot.migrate.core.job;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.time.StopWatch;
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
        StopWatch started = StopWatch.createStarted();
        for (MigratePosition position : positions) {
            MigrateTask migrateTask = MigrateTaskFactory.newTask(context, jobConfig, position);
            //migrateTask.run();
            context.getTaskExecuteEngine().submit(migrateTask, null);
        }
        context.getTaskExecuteEngine().awaitFinish();

        started.stop();
        System.err.println(started.getTime(TimeUnit.SECONDS));
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
