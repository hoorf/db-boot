package org.github.hoorf.dbboot.migrate.core.task.inventory;

import java.util.Optional;
import lombok.Getter;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;
import org.github.hoorf.dbboot.migrate.core.channel.MemoryChannel;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.dumper.Dumper;
import org.github.hoorf.dbboot.migrate.core.dumper.DumperLoader;
import org.github.hoorf.dbboot.migrate.core.imoprter.Importer;
import org.github.hoorf.dbboot.migrate.core.imoprter.ImporterLoader;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.position.PlaceholderPosition;
import org.github.hoorf.dbboot.migrate.core.record.Record;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTask;

public class InventoryTask implements MigrateTask {

    @Getter
    private String taskId;

    //执行到channel里面哪个位置
    private MigratePosition<?> position;

    private MigrateContext context;

    private Dumper dumper;

    private Importer importer;

    private InventoryJobConfig jobConfig;

    public InventoryTask(InventoryJobConfig jobConfig) {
        this.jobConfig = jobConfig;
        this.dumper = DumperLoader.getInstance(jobConfig.getType());
        this.importer = ImporterLoader.getInstance(jobConfig.getType());
    }

    @Override
    public void setContext(MigrateContext context) {
        this.context = context;
    }

    @Override
    public InventoryProgress getProgress() {
        return new InventoryProgress(position);
    }

    @Override
    public void start() {
        run();
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        MemoryChannel channel = new MemoryChannel(records -> {
            Optional<Record> record = records.stream().filter(each -> !(each.getPosition() instanceof PlaceholderPosition)).reduce((a, b) -> b);
            record.ifPresent(value -> position = value.getPosition());
        });
        dumper.setChannel(channel);
        importer.setChannel(channel);
        context.getExecuteEngine().submit(importer, null, e -> dumper.stop());
        dumper.start();
    }
}
