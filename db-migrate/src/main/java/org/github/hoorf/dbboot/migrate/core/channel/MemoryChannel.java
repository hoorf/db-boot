package org.github.hoorf.dbboot.migrate.core.channel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.github.hoorf.dbboot.migrate.core.record.Record;

public class MemoryChannel implements MigrateChannel {

    private BlockingQueue<Record> queue = new ArrayBlockingQueue<>(5000);

    private Consumer<List<Record>> ackCallback;

    private List<Record> toBeAcknowledgeRecords = new LinkedList<>();

    public MemoryChannel(Consumer<List<Record>> ackCallback) {
        this.ackCallback = ackCallback;
    }

    @Override
    public void push(Record record) throws InterruptedException {
        queue.put(record);
    }

    @SneakyThrows
    @Override
    public List<Record> pull(int batchSize, int timeout) {
        List<Record> result = new ArrayList<>(batchSize);
        long start = System.currentTimeMillis();
        while (batchSize > queue.size()) {
            if (System.currentTimeMillis() - start >= timeout * 1000L) {
                break;
            }
            Thread.sleep(100L);
        }
        queue.drainTo(result);
        return result;
    }

    @Override
    public void ack() {
        if (!toBeAcknowledgeRecords.isEmpty()) {
            ackCallback.accept(toBeAcknowledgeRecords);
            toBeAcknowledgeRecords.clear();
        }
    }

    @Override
    public void close() {
        queue.clear();
    }


}
