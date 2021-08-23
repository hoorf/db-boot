package org.github.hoorf.dbboot.core.writer.context;

import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.config.ShardingAlgorithmConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableConfiguration;
import org.github.hoorf.dbboot.config.ShardingTableTargetConfiguration;
import org.github.hoorf.dbboot.core.router.Router;
import org.github.hoorf.dbboot.core.router.StandardRouter;
import org.github.hoorf.dbboot.spi.typed.TypedSPIRegistry;

import java.util.Map;

@Getter
@Setter
public class WriteAlgorithm {

    private String dataSourceName;

    private String table;

    @Getter
    private Router dataSourceRouter;

    @Getter
    private Router tableRouter;

    public WriteAlgorithm(ShardingTableConfiguration config) {
        ShardingTableTargetConfiguration targetConfig = config.getTarget();
        ShardingTableTargetConfiguration.DatasourceStrategyConfiguration dataSourceStrategy = targetConfig.getDataSourceStrategy();
        if (null != dataSourceStrategy) {
            dataSourceRouter = TypedSPIRegistry.findRegisteredService(StandardRouter.class, dataSourceStrategy.getType(), dataSourceStrategy.getProps()).orElseThrow(RuntimeException::new);
        } else {
            dataSourceName = targetConfig.getDataSourceName();
        }
        ShardingTableTargetConfiguration.TableStrategyConfiguration tableStrategy = targetConfig.getTableStrategy();
        if (null != tableStrategy) {
            tableRouter = TypedSPIRegistry.findRegisteredService(StandardRouter.class, tableStrategy.getType(), tableStrategy.getProps()).orElseThrow(RuntimeException::new);
        } else {
            table = targetConfig.getTableName();
        }
    }

    public String getTable(Map<String, Object> data) {
        return tableRouter == null ? table : tableRouter.route(data);
    }

    public String getDataSourceName(Map<String, Object> data) {
        return dataSourceRouter == null ? dataSourceName : dataSourceRouter.route(data);
    }


}
