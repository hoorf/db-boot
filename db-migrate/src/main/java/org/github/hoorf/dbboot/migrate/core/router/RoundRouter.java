package org.github.hoorf.dbboot.migrate.core.router;

import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.github.hoorf.dbboot.migrate.core.record.DataRecord;

public class RoundRouter implements Router {

    private static final String PROPS_ROUND_NAMES = "roundNames";

    private String[] keys;

    private int index = 0;


    @Override
    public String route(DataRecord dataRecord) {
        String result = keys[index];
        index = (++index) % keys.length;
        return result;
    }


    @Override
    public String getType() {
        return "ROUND";
    }

    @Override
    public void setProps(Properties props) {
        String roundNames = props.getProperty(PROPS_ROUND_NAMES);
        keys = StringUtils.split(roundNames, ",");
    }
}
