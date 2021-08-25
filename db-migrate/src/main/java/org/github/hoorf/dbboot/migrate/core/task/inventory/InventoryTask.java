package org.github.hoorf.dbboot.migrate.core.task.inventory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import org.github.hoorf.dbboot.migrate.core.channel.MemoryChannel;
import org.github.hoorf.dbboot.migrate.core.dumper.Dumper;
import org.github.hoorf.dbboot.migrate.core.executor.ExecuteEngine;
import org.github.hoorf.dbboot.migrate.core.imoprter.Importer;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.position.PlaceholderPosition;
import org.github.hoorf.dbboot.migrate.core.record.Record;
import org.github.hoorf.dbboot.migrate.core.task.MigrateTask;

public class InventoryTask implements MigrateTask {

    @Getter
    private String taskId;

    //执行到channel里面哪个位置
    private MigratePosition<?> position;

    private ExecuteEngine executeEngine;

    private Dumper dumper;

    private Importer importer;


    public InventoryTask(Dumper dumper) {
        executeEngine = ExecuteEngine.newCachedThreadPool();
    }

    @Override
    public InventoryProgress getProgress() {
        return new InventoryProgress(position);
    }

    @Override
    public void start() {
        MemoryChannel channel = new MemoryChannel(records -> {
            Optional<Record> record = records.stream().filter(each -> !(each.getPosition() instanceof PlaceholderPosition)).reduce((a, b) -> b);
            record.ifPresent(value -> position = value.getPosition());
        });
        dumper.setChannel(channel);
        importer.setChannel(channel);
        executeEngine.submit(importer, null, e -> dumper.stop());
        dumper.start();

    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {

    }
}
