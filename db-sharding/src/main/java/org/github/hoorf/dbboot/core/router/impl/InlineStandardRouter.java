package org.github.hoorf.dbboot.core.router.impl;

import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.core.router.StandardRouter;
import org.github.hoorf.dbboot.util.GroovyUtils;

import java.util.*;

public class InlineStandardRouter implements StandardRouter {

    private static final String PROPS_SHARDING_COLUMNS = "shardingColumns";
    private static final String PROPS_ALGORITHM_EXPRESSION = "algorithm-expression";

    private String evalRule;

    private Set<String> keys = new HashSet<>();

    @Override
    public String route(Map<String, Object> row) {
        // Preconditions.checkArgument(keyPair.keySet().containsAll(keys), "not support key [%s] for route", keyPair.keySet());
        String result = GroovyUtils.eval(evalRule, row);
        // log.debug("route result [{}] for {}", result, keyPair.keySet());
        return result;
    }

    @Override
    public String getType() {
        return INLINE;
    }

    @Override
    public void setProps(Properties props) {
        evalRule = props.getProperty(PROPS_ALGORITHM_EXPRESSION);
        keys.addAll(Arrays.asList(StringUtils.split(props.getProperty(PROPS_SHARDING_COLUMNS), ",")));
    }
}
