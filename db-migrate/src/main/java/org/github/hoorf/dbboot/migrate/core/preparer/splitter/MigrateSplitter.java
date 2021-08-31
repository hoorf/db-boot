package org.github.hoorf.dbboot.migrate.core.preparer.splitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.position.RangePosition;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilder;
import org.github.hoorf.dbboot.migrate.core.sqlbuilder.MigrateSQLBuilderLoader;

@Slf4j
public class MigrateSplitter {

    public List<MigratePosition> splitter(MigrateContext context) {
        List<MigratePosition> result = new ArrayList<>();
        DataSourceWrapper dataSource = context.getDataSourceManager().getDataSource(context.getJobConfig().getSource().getDataSourceName());
        MigrateSQLBuilder sqlBuilder = MigrateSQLBuilderLoader.getInstance(dataSource.getDatabaseType());
        String sql = sqlBuilder.buildSelectPkRangeSQL(context.getJobConfig().getSource().getTable(), context.getJobConfig().getSource().getPk());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            long begin = 0;
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                ps.setLong(1, begin);
                ps.setLong(2, context.getJobConfig().getSource().getDumpConfig().getBatchSize());
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                long endPk = resultSet.getLong(1);
                if (endPk == 0) {
                    break;
                }
                result.add(new RangePosition(begin, endPk));
                begin = endPk + 1;
            }

            if (0 == result.size()) {
                result.add(new RangePosition(0, 0));
            }
        } catch (SQLException e) {
            log.error("splitter error", e);
        }
        return result;
    }
}
