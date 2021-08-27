package org.github.hoorf.dbboot.migrate.core.imoprter;

import ch.qos.logback.classic.db.SQLBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.github.hoorf.dbboot.migrate.config.JobConfig;
import org.github.hoorf.dbboot.migrate.core.AbstractMigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.record.FinishedRecord;
import org.github.hoorf.dbboot.migrate.core.record.Record;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilder;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilderLoader;

@Slf4j
public abstract class AbstractImporter extends AbstractMigrateExecutor implements Importer {

    @Setter
    protected MigrateContext context;

    @Setter
    private MigrateChannel channel;

    @Override
    public void run() {
        while (isRunning()) {
            List<Record> records = channel.pull(1024, 3);
            if (null != records && records.size() > 0) {
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
            log.error("", e);
        }
    }

    private void write(List<Record> records) throws SQLException {
        DataSourceWrapper dataSource = context.getDataSourceManager().getDataSource(context.getJobConfig().getTarget());
        try (Connection connection = dataSource.getConnection()) {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            batchInsert(dataSource, connection, records);
            connection.commit();
        }
    }

    protected void batchInsert(DataSourceWrapper dataSource, Connection connection, List<Record> records) throws SQLException {
        MigrateSQLBuilder sqlBuilder = MigrateSQLBuilderLoader.getInstance(dataSource.getDatabaseType());
        List<DataRecord> dataRecords = records.stream().filter(each -> each instanceof DataRecord).map(each -> (DataRecord) each).collect(Collectors.toList());
        if (dataRecords.size() > 0) {
            String insertSql = sqlBuilder.buildInsertSQL(dataRecords.get(0));
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
