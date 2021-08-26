package org.github.hoorf.dbboot.migrate.core.preparer;

import java.sql.SQLException;
import java.util.List;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.preparer.splitter.MigrateSplitter;

public class MigratePreparer {

    MigrateSplitter splitter = new MigrateSplitter();

    public List<MigratePosition> prepare(MigrateContext context) {
        checkDataSource(context.getDataSourceManager().getDataSource(context.getSourceName()));
        checkDataSource(context.getDataSourceManager().getDataSource(context.getTargetName()));
        List<MigratePosition> positions = this.splitter.splitter(context);
        return positions;
    }

    private void checkDataSource(DataSourceWrapper dataSource) {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
