package org.github.hoorf.dbboot.core;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.github.hoorf.dbboot.config.ShardingDataSourceConfiguration;
import org.github.hoorf.dbboot.config.ShardingGlobalConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableSourceConfiguration;
import org.github.hoorf.dbboot.core.reader.Reader;
import org.github.hoorf.dbboot.core.writer.Writer;
import org.github.hoorf.dbboot.spi.DbServiceLoader;
import org.github.hoorf.dbboot.util.DbUtils;
import org.github.hoorf.dbboot.util.RangeSpiltUtils;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ShardingProcess {
    private static String SQL_SEARCH = "select * from %s where %s between ? and  ? ";
    private DataSource dataSource;
    private String tableName;
    private String pk;

    private List<Pair<Object, Object>> sqlRanges;
    private Pair<Object, Object> minMaxPk;
    private Triple<List<String>, List<Integer>, List<String>> columnMetaData;
    private Integer taskSize;
    private Map<String, ShardingDataSourceConfiguration> dataSources;
    private ShardingTableConfiguration shardingTableConfiguration;

    private ExecutorService executor = new ThreadPoolExecutor(30, 100, 6L, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());

    public void config(ShardingTableConfiguration shardingTableConfiguration, Map<String, ShardingDataSourceConfiguration> dataSources, ShardingGlobalConfiguration globalConfig) {
        ShardingTableSourceConfiguration source = shardingTableConfiguration.getSource();
        this.tableName = source.getTableName();
        this.pk = source.getPk();
        ShardingDataSourceConfiguration dataSourceConfiguration = dataSources.get(source.getDataSourceName());
        Preconditions.checkNotNull(dataSourceConfiguration);
        this.dataSource = DbUtils.buildDataSource(dataSourceConfiguration.getUrl(), dataSourceConfiguration.getDriverClass(), dataSourceConfiguration.getUsername(), dataSourceConfiguration.getPassword());
        this.dataSources = dataSources;
        this.shardingTableConfiguration = shardingTableConfiguration;
        this.taskSize = globalConfig.getTaskSize();
    }


    @SneakyThrows
    private void init() {
        prepare();
        spiltTask();
    }

    @SneakyThrows
    public void start() {
        init();
        ShardingContext shardingContext = new ShardingContext(shardingTableConfiguration, dataSources, columnMetaData);
        shardingContext.start();
        createWriter(shardingContext).forEach(writer -> executor.submit(writer));
        createReader(shardingContext).forEach(reader -> executor.submit(reader));
        while (!shardingContext.isFinished()) {
            TimeUnit.SECONDS.sleep(1);
        }
        executor.shutdown();
    }

    private List<Reader> createReader(ShardingContext shardingContext) {
        List<Reader> readers = new ArrayList<>();
        String sql = String.format(SQL_SEARCH, tableName, pk);
        for (Pair<Object, Object> pk : sqlRanges) {
            Reader reader = DbServiceLoader.newServiceInstances(Reader.class).iterator().next();
            reader.init(sql, pk, shardingContext);
            readers.add(reader);
        }
        shardingContext.addReaders(readers);
        return readers;
    }

    private List<Writer> createWriter(ShardingContext shardingContext) {
        List<Writer> writers = new ArrayList<>();
        for (int i = 0; i < sqlRanges.size(); i++) {
            Writer writer = DbServiceLoader.newServiceInstances(Writer.class).iterator().next();
            writer.init(shardingContext);
            writers.add(writer);
        }
        shardingContext.addWriters(writers);
        return writers;
    }

    private void spiltTask() {
        Preconditions.checkState(null != taskSize && taskSize > 0, "taskSize setting not correct");
        Object min = minMaxPk.getLeft();
        Object max = minMaxPk.getRight();
        Object[] ranges = RangeSpiltUtils.spilt(min, max, taskSize - 1 > 0 ? taskSize - 1 : 1);
        buildSqlTasks(ranges);
    }

    private void buildSqlTasks(Object[] ranges) {
        Preconditions.checkNotNull(ranges);
        Preconditions.checkState(null != tableName && null != pk, "table [%s] pk [%s] should not be null", tableName, pk);
        sqlRanges = new ArrayList<>();
        for (int i = 0; i < ranges.length - 1; i++) {
            sqlRanges.add(new ImmutablePair<>(ranges[i], ranges[i + 1]));
        }
        log.debug("SQL task : {}", sqlRanges);
    }

    @SneakyThrows
    private void prepare() {
        Connection connection = dataSource.getConnection();
        columnMetaData = DbUtils.getColumnMetaData(connection, tableName);
        minMaxPk = DbUtils.getTableMinMaxPk(connection, tableName, pk);
        DbUtils.closeDbResources(connection);
    }

}
