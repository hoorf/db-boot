package org.github.hoorf.dbboot.core.reader;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.github.hoorf.dbboot.core.DataRecord;
import org.github.hoorf.dbboot.core.ShardingContext;
import org.github.hoorf.dbboot.jdbc.TypeHandler;
import org.github.hoorf.dbboot.jdbc.registry.TypeHandlerRegistry;
import org.github.hoorf.dbboot.util.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultReader extends AbstractReader implements Reader {

    private ShardingContext shardingContext;

    private String sql;

    private Pair<Object, Object> pkRange;

    @Override
    public void init(String sql, Pair<Object, Object> pkRange, ShardingContext shardingContext) {
        this.sql = sql;
        this.shardingContext = shardingContext;
        this.pkRange = pkRange;
    }

    @Override
    public void run() {
        try {
            startMonitor();
            run0();
            fastFinish();
            summary();
        } catch (Exception e) {
            fastFail();
            log.error("", e);
        }
    }

    @SneakyThrows
    private void run0() {
        DataSource dataSource = shardingContext.getDataSource(shardingContext.getShardingTableConfiguration().getSource().getDataSourceName());
        Triple<List<String>, List<Integer>, List<String>> recordMetadata = shardingContext.getRecordMetadata();
        List<String> labelNames = recordMetadata.getLeft();
        Connection connection = dataSource.getConnection();

        connection.setAutoCommit(false);
        PreparedStatement statement = DbUtils.prepareReadStatement(connection, sql);
        Object min = pkRange.getLeft();
        Object max = pkRange.getRight();
        TypeHandler typeHandler = TypeHandlerRegistry.getTypeHandler(min.getClass());
        typeHandler.setParameter(statement, 1, min, null);
        typeHandler.setParameter(statement, 2, max, null);
        log.debug("sql:{}", statement.toString());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Map<String, Object> dataMap = Maps.newHashMap();
            for (String labelName : labelNames) {
                dataMap.put(labelName, resultSet.getObject(labelName));
            }
            shardingContext.push(new DataRecord(dataMap));
            countRecord();
        }
        DbUtils.closeDbResources(connection);
    }

}
