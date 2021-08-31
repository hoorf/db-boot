package org.github.hoorf.dbboot.migrate.core.preparer;

import com.google.common.base.Preconditions;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.migrate.core.context.DataSourceWrapper;
import org.github.hoorf.dbboot.migrate.core.context.MigrateContext;
import org.github.hoorf.dbboot.migrate.core.context.meta.TableMeta;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;
import org.github.hoorf.dbboot.migrate.core.preparer.splitter.MigrateSplitter;

public class MigratePreparer {

    MigrateSplitter splitter = new MigrateSplitter();

    public List<MigratePosition> prepare(MigrateContext context) {
        checkDataSource(context.getDataSourceManager().getDataSource(context.getJobConfig().getSource().getDataSourceName()));
//        checkDataSource(context.getDataSourceManager().getDataSource(context.getJobConfig().getTarget().getDataSourceName()));
        checkPk(context);
        List<MigratePosition> positions = this.splitter.splitter(context);
        return positions;
    }

    private void checkPk(MigrateContext context) {
        if (StringUtils.isEmpty(context.getJobConfig().getSource().getPk())) {
            TableMeta tableMeta = context.getDataSourceManager().getTableMeta(context.getJobConfig().getSource().getDataSourceName(), context.getJobConfig().getSource().getTable());
            String pk = tableMeta.getPrimaryKeys().get(0);
            Preconditions.checkState(StringUtils.isNotBlank(pk), "cloud not found pk for table[%s]", context.getJobConfig().getSource().getTable());
            context.getJobConfig().getSource().setPk(pk);
        }
    }

    private void checkDataSource(DataSourceWrapper dataSource) {
        try {
            dataSource.getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
