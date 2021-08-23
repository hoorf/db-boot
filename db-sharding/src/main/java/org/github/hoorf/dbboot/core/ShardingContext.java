package org.github.hoorf.dbboot.core;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.github.hoorf.dbboot.common.StateCtrl;
import org.github.hoorf.dbboot.config.ShardingDataSourceConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableConfiguration;
import org.github.hoorf.dbboot.core.reader.Reader;
import org.github.hoorf.dbboot.core.writer.AbstractWriter;
import org.github.hoorf.dbboot.core.writer.Writer;
import org.github.hoorf.dbboot.util.DbUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class ShardingContext extends StateCtrl {

    private List<Reader> readers = new ArrayList<>();

    private List<Writer> writers = new ArrayList<>();

    private BlockingQueue<DataRecord> queue = new LinkedBlockingQueue<>();

    private Map<String, DataSource> dataSourceMap = new HashMap<>();

    @Getter
    private Triple<List<String>, List<Integer>, List<String>> recordMetadata;

    @Getter
    private ShardingTableConfiguration shardingTableConfiguration;

    private Map<String, ShardingDataSourceConfiguration> dataSources;

    public ShardingContext(ShardingTableConfiguration shardingTableConfiguration, Map<String, ShardingDataSourceConfiguration> dataSources, Triple<List<String>, List<Integer>, List<String>> sourceMetadata) {
        this.shardingTableConfiguration = shardingTableConfiguration;
        this.dataSources = dataSources;
        this.recordMetadata = sourceMetadata;
    }

    public DataSource getDataSource(String dataSourceName) {
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (null == dataSource) {
            ShardingDataSourceConfiguration dataSourceConfiguration = dataSources.get(dataSourceName);
            Preconditions.checkNotNull(dataSourceConfiguration);
            dataSource = DbUtils.buildDataSource(dataSourceConfiguration.getUrl(), dataSourceConfiguration.getDriverClass(), dataSourceConfiguration.getUsername(), dataSourceConfiguration.getPassword());
            dataSourceMap.put(dataSourceName, dataSource);
        }
        return dataSource;
    }

    public void addReaders(List<Reader> readers) {
        this.readers.addAll(readers);
    }

    public void addWriters(List<Writer> writers) {
        this.writers.addAll(writers);
    }

    public synchronized int pull(List<DataRecord> store) {
        int total = queue.drainTo(store, 10000);
        if (total > 0) {
            log.debug("pull {} records", total);
        }
        return total;
    }

    public synchronized void push(DataRecord record) {
        queue.offer(record);
    }

    public synchronized void push(List<DataRecord> dataRecords) {
        log.debug("receive {} records", dataRecords.size());
        queue.addAll(dataRecords);
    }

    public synchronized boolean hasNotDeal() {
        return queue.size() > 0;
    }

    public void start() {
        Preconditions.checkState(setState(STATE_INIT, STATE_RUNNING), "has been stated");
        start0();
    }

    private void start0() {

    }

    public boolean readerFinished() {
        for (Reader reader : readers) {
            if (!reader.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public boolean writerFinished() {
        if (!readerFinished()) {
            return false;
        }
        for (Writer writer : writers) {
            if (!writer.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFinished() {
        if (super.isFinished()) {
            return true;
        } else {
            boolean finished = readerFinished() && writerFinished();
            if (finished) {
                forceState(STATE_FINISH);
            }
            return finished;
        }
    }
}
