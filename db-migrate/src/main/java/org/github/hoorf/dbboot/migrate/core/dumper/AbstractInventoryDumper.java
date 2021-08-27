package org.github.hoorf.dbboot.migrate.core.dumper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Optional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.github.hoorf.dbboot.migrate.config.task.InventoryJobConfig;
import org.github.hoorf.dbboot.migrate.core.AbstractMigrateExecutor;
import org.github.hoorf.dbboot.migrate.core.channel.MigrateChannel;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.context.meta.TableMeta;
import org.github.hoorf.dbboot.migrate.core.position.FinishedPosition;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.position.PlaceholderPosition;
import org.github.hoorf.dbboot.migrate.core.position.RangePosition;
import org.github.hoorf.dbboot.migrate.core.record.Column;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.core.record.FinishedRecord;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilderLoader;

@Slf4j
public abstract class AbstractInventoryDumper extends AbstractMigrateExecutor implements InventoryDumper {

    @Setter
    protected MigrateContext context;

    @Setter
    private MigrateChannel channel;

    @Setter
    private InventoryJobConfig config;

    @Override
    public void run() {
        DataSourceWrapper dataSource = context.getDataSourceManager().getDataSource(config.getSource());
        TableMeta tableMeta = context.getDataSourceManager().getTableMeta(dataSource, config.getTable());
        String pk = Optional.ofNullable(config.getPk()).orElseGet(() -> tableMeta.getPrimaryKeys().get(0));
        String sql = MigrateSQLBuilderLoader.getInstance(dataSource.getDatabaseType()).buildSelectSQL(config.getTable(), pk);
        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = preparedStatement(connection, sql,(RangePosition)config.getPosition());
            ResultSet resultSet = statement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                DataRecord record = new DataRecord(copyMigratePosition(config.getPosition()));
                record.setTableName(config.getTable());
                for (int index = 1; index <= metaData.getColumnCount(); index++) {
                    String columnName = metaData.getColumnName(index);
                    record.addColumn(new Column(columnName, resultSet.getObject(index), tableMeta.isPrimaryKey(columnName)));
                }
                channel.push(record);
            }
            channel.push(new FinishedRecord(new FinishedPosition()));
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    protected MigratePosition copyMigratePosition(MigratePosition migratePosition) {
        if (migratePosition instanceof RangePosition) {
            return new RangePosition(((RangePosition) migratePosition).getBeginValue(), ((RangePosition) migratePosition).getEndValue());
        }
        return new PlaceholderPosition();
    }

    private PreparedStatement preparedStatement(Connection connection, String sql,RangePosition position) throws SQLException{
        PreparedStatement statement = createStatement(connection, sql);
        statement.setLong(1,position.getBeginValue());
        statement.setLong(2,position.getEndValue());
        return statement;
    }

    protected abstract PreparedStatement createStatement(Connection connection, String sql) throws SQLException;

}
