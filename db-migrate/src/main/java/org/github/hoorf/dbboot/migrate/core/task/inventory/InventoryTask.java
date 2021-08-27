package org.github.hoorf.dbboot.migrate.core.task.inventory;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;
import org.github.hoorf.dbboot.migrate.core.AbstractMigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MemoryChannel;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.dumper.DumperFactory;
import org.github.hoorf.dbboot.migrate.core.dumper.InventoryDumper;
import org.github.hoorf.dbboot.migrate.core.imoprter.Importer;
import org.github.hoorf.dbboot.migrate.core.imoprter.ImporterFactory;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.position.PlaceholderPosition;
import org.github.hoorf.dbboot.migrate.core.record.Record;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTask;

@Slf4j
public class InventoryTask extends AbstractMigrateExecutor implements MigrateTask {

    @Getter
    private String taskId = UUID.randomUUID().toString();

    //执行到channel里面哪个位置
    private MigratePosition<?> position;

    private MigrateContext context;

    private InventoryDumper dumper;

    private Importer importer;

    private InventoryJobConfig jobConfig;

    public InventoryTask(InventoryJobConfig jobConfig) {
        this.jobConfig = jobConfig;
        this.dumper = DumperFactory.getInventoryInstance(jobConfig.getDumperType());
        this.dumper.setConfig(jobConfig);
        this.importer = ImporterFactory.getInstance(jobConfig.getImporterType());
    }

    @Override
    public void setContext(MigrateContext context) {
        this.context = context;
        dumper.setContext(context);
        importer.setContext(context);
    }

    @Override
    public InventoryProgress getProgress() {
        return new InventoryProgress(position);
    }


    @Override
    public void run() {
        start();
        MemoryChannel channel = new MemoryChannel(records -> {
            Optional<Record> record = records.stream().filter(each -> !(each.getPosition() instanceof PlaceholderPosition)).reduce((a, b) -> b);
            record.ifPresent(value -> position = value.getPosition());
        });
        dumper.setChannel(channel);
        importer.setChannel(channel);
        dumper.start();
        importer.start();
        //dumper.run();
        //importer.run();
        context.getExecuteEngine().submit(dumper, null, null);
        context.getExecuteEngine().submit(importer, Void -> {
            log.error("-----------------success dumper for {}-----------------------",taskId);
        }, e -> {
            log.error("-----------------error for -----------------------");
            dumper.stop();
        });
    }
}
