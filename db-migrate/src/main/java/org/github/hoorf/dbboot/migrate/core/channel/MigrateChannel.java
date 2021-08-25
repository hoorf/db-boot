package org.github.hoorf.dbboot.migrate.core.channel;

import java.util.List;
import org.github.hoorf.dbboot.migrate.core.record.Record;

public interface MigrateChannel {

    void push(Record record) throws InterruptedException;

    List<Record> pull(int batchSize, int timeout);

    void ack();

    void close();
}
