package org.github.hoorf.dbboot.migrate.core.router;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.migrate.core.record.Column;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;
import org.github.hoorf.dbboot.migrate.utils.GroovyUtils;

public class InlineRouter implements Router {

    private static final String PROPS_SHARDING_COLUMNS = "shardingColumns";

    private static final String PROPS_ALGORITHM_EXPRESSION = "algorithm-expression";

    private String evalRule;

    private Set<String> keys = new HashSet<>();

    @Override
    public String route(DataRecord dataRecord) {
        // Preconditions.checkArgument(keyPair.keySet().containsAll(keys), "not support key [%s] for route", keyPair.keySet());
        Map<String, Object> valueMap = dataRecord.getColumns().stream().filter(each -> keys.contains(each.getName())).collect(Collectors.toMap(Column::getName, Column::getValue));
        String result = GroovyUtils.eval(evalRule, valueMap);
        // log.debug("route result [{}] for {}", result, keyPair.keySet());
        return result;
    }

    @Override
    public String getType() {
        return "INLINE";
    }

    @Override
    public void setProps(Properties props) {
        evalRule = props.getProperty(PROPS_ALGORITHM_EXPRESSION);
        keys.addAll(Arrays.asList(StringUtils.split(props.getProperty(PROPS_SHARDING_COLUMNS), ",")));
    }
}
