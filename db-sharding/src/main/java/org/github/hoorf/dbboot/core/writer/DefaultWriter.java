package org.github.hoorf.dbboot.core.writer;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.github.hoorf.dbboot.core.DataRecord;
import org.github.hoorf.dbboot.core.ShardingContext;
import org.github.hoorf.dbboot.core.writer.context.WriteAlgorithm;
import org.github.hoorf.dbboot.jdbc.TypeHandler;
import org.github.hoorf.dbboot.jdbc.registry.TypeHandlerRegistry;
import org.github.hoorf.dbboot.util.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultWriter extends AbstractWriter implements Writer {

    private ShardingContext shardingContext;

    private WriteAlgorithm writeAlgorithm;

    private Map<String, Triple<Connection, PreparedStatement, Integer>> processCache = new HashMap<>();

    @Override
    public void init(ShardingContext shardingContext) {
        this.shardingContext = shardingContext;
        writeAlgorithm = new WriteAlgorithm(shardingContext.getShardingTableConfiguration());
    }

    @Override
    public void run() {
        try {
            startMonitor();
            run0();
            fastFinish();
            summary();
        } catch (Exception e) {
            log.error("", e);
            fastFail();
        }
    }

    @SneakyThrows
    private void run0() {
        do {
            List<DataRecord> records = Lists.newArrayList();
            shardingContext.pull(records);
            if (records.size() > 0) {
                write(records);
            }
        } while (!shardingContext.readerFinished() || shardingContext.hasNotDeal());
        for (Triple<Connection, PreparedStatement, Integer> triple : processCache.values()) {
            if (triple.getRight() > 0) {
                commit(triple.getMiddle());
            }
            DbUtils.closeDbResources(null, triple.getMiddle(), triple.getLeft());
        }
    }


    @SneakyThrows
    private void write(List<DataRecord> records) {
        if (records.size() == 0) {
            return;
        }
        StopWatch stopWatch = StopWatch.createStarted();
        Triple<List<String>, List<Integer>, List<String>> recordMetadata = shardingContext.getRecordMetadata();
        for (DataRecord record : records) {
            String dataSourceName = writeAlgorithm.getDataSourceName(record.getData());
            String table = writeAlgorithm.getTable(record.getData());
            Triple<Connection, PreparedStatement, Integer> dealTriple = processCache.get(dataSourceName);
            Connection connection;
            PreparedStatement preparedStatement;
            Integer dealCount = 0;
            if (null == dealTriple) {
                DataSource dataSource = shardingContext.getDataSource(dataSourceName);
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                String sql = genInsertSql(table, recordMetadata.getLeft());
                preparedStatement = connection.prepareStatement(sql);
            } else {
                connection = dealTriple.getLeft();
                preparedStatement = dealTriple.getMiddle();
                dealCount = dealTriple.getRight();
            }
            countRecord();
            dealCount++;
            if (routeWrite0(preparedStatement, recordMetadata, record, dealCount)) {
                dealCount = 0;
            }
            processCache.put(dataSourceName, new ImmutableTriple<>(connection, preparedStatement, dealCount));
        }
        log.debug("deal {} record  for {} seconds", records.size(), stopWatch.getTime(TimeUnit.SECONDS));
    }

    @SneakyThrows
    private Boolean routeWrite0(PreparedStatement preparedStatement, Triple<List<String>, List<Integer>, List<String>> recordMetadata, DataRecord record, int count) {
        boolean hasCommit = false;
        preparedRecord(preparedStatement, recordMetadata, record);
        preparedStatement.addBatch();
        if (count > 500) {
            commit(preparedStatement);
            hasCommit = true;
        }
        return hasCommit;
    }

    private void commit(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeBatch();
        preparedStatement.getConnection().commit();
        preparedStatement.clearBatch();
    }

    private void preparedRecord(PreparedStatement preparedStatement, Triple<List<String>, List<Integer>, List<String>> recordMetadata, DataRecord record) {
        for (int i = 0; i < recordMetadata.getMiddle().size(); i++) {
            Integer metadataType = recordMetadata.getMiddle().get(i);
            String columnName = recordMetadata.getLeft().get(i);
            Object value = record.getData().get(columnName);
            setData(preparedStatement, columnName, value, metadataType, i + 1);
        }
    }

    @SneakyThrows
    private void setData(PreparedStatement preparedStatement, String columnName, Object value, Integer metadataType, int columnIndex) {
        if (null == value) {
            preparedStatement.setNull(columnIndex, metadataType);
            return;
        }
//        Class<?> valueClass = value.getClass();
//        JDBCType jdbcType = JDBCType.valueOf(metadataType);
//        TypeHandler typeHandler = TypeHandlerRegistry.getTypeHandler(valueClass, jdbcType);
//        typeHandler.setParameter(preparedStatement, columnIndex, value, jdbcType);

        preparedStatement.setObject(columnIndex,value);
    }
}
