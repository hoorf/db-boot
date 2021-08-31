package org.github.hoorf.dbboot.migrate.core.imoprter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.AbstractMigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.record.FinishedRecord;
import org.github.hoorf.dbboot.migrate.core.record.Record;
import org.github.hoorf.dbboot.migrate.core.router.Router;
import org.github.hoorf.dbboot.migrate.core.router.RouterFactory;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilder;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilderLoader;
import org.github.hoorf.dbboot.migrate.monitor.RecordSpeedCounter;

public abstract class AbstractImporter extends AbstractMigrateExecutor implements Importer {

    @Setter
    protected MigrateContext context;

    @Setter
    private MigrateChannel channel;

    @Override
    public void run0() {
        while (isRunning()) {
            List<Record> records = channel.pull(1024, 3);
            if (null != records && records.size() > 0) {
                RecordSpeedCounter.countImporter(records.size());
                tryFlush(records);
                if (records.get(records.size() - 1) instanceof FinishedRecord) {
                    channel.ack();
                    break;
                }
            }

        }
        channel.ack();
    }

    private void tryFlush(List<Record> records) {
        try {
            write(records);
        } catch (Exception e) {
            stop();
            logger.error("", e);
        }
    }

    private void write(List<Record> records) throws SQLException {
        JobConfig.DataSourceStrategyConfiguration dataSourceStrategy = context.getJobConfig().getTarget().getDataSourceStrategy();
        JobConfig.TableStrategyConfiguration tableStrategy = context.getJobConfig().getTarget().getTableStrategy();
        Router dataSourceRouter = RouterFactory.getInstance(dataSourceStrategy);
        Router tableRouter = RouterFactory.getInstance(tableStrategy);
        Map<String, Map<String, List<DataRecord>>> result = new HashMap<>();
        records.stream().filter(each -> each instanceof DataRecord).map(each -> (DataRecord) each).forEach(each -> {
            String dataSourceName = StringUtils.defaultIfEmpty(dataSourceRouter.route(each), context.getJobConfig().getTarget().getDataSourceName());
            String tableName = StringUtils.defaultIfEmpty(tableRouter.route(each), context.getJobConfig().getTarget().getTable());
            Map<String, List<DataRecord>> tableMap = result.computeIfAbsent(dataSourceName, k -> new HashMap());
            List<DataRecord> dataRecords = tableMap.computeIfAbsent(tableName, k -> new ArrayList<>());
            dataRecords.add(each);
        });
        for (String dataSourceName : result.keySet()) {
            for (String tableName : result.get(dataSourceName).keySet()) {
                List<DataRecord> dataRecords = result.get(dataSourceName).get(tableName);
                write(dataRecords, dataSourceName, tableName);
            }
        }
    }


    private void write(List<DataRecord> records, String dataSourceName, String tableName) throws SQLException {
        DataSourceWrapper dataSource = context.getDataSourceManager().getDataSource(dataSourceName);
        try (Connection connection = dataSource.getConnection()) {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            batchInsert(dataSource, connection, tableName, records);
            connection.commit();
        }
    }

    protected void batchInsert(DataSourceWrapper dataSource, Connection connection, String tableName, List<DataRecord> dataRecords) throws SQLException {
        MigrateSQLBuilder sqlBuilder = MigrateSQLBuilderLoader.getInstance(dataSource.getDatabaseType());
        if (dataRecords.size() > 0) {
            String insertSql = sqlBuilder.buildInsertOrUpdateSQL(dataRecords.get(0), tableName);
            logger.debug(insertSql);
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setQueryTimeout(30);
                for (DataRecord dataRecord : dataRecords) {
                    for (int i = 0; i < dataRecord.getColumns().size(); i++) {
                        ps.setObject(i + 1, dataRecord.getColumn(i).getValue());
                    }
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }
}
