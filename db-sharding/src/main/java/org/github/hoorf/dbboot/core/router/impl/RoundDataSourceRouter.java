package org.github.hoorf.dbboot.core.router.impl;

import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.core.router.StandardRouter;

import java.util.Map;
import java.util.Properties;

public class RoundDataSourceRouter implements StandardRouter {

    private static final String PROPS_ROUND_NAMES = "roundNames";

    private String[] keys;

    private int index = 0;


    @Override
    public String route(Map<String, Object> row) {
        String result = keys[index];
        index = (++index) % keys.length;
        return result;
    }


    @Override
    public String getType() {
        return ROUND;
    }

    @Override
    public void setProps(Properties props) {
        String roundNames = props.getProperty(PROPS_ROUND_NAMES);
        keys = StringUtils.split(roundNames, ",");
    }
}
