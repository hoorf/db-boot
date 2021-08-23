package org.github.hoorf.dbboot.core.writer;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import org.github.hoorf.dbboot.common.StatisticsCtrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWriter extends StatisticsCtrl {

    protected final static String SQL_PARAM_HOLDER = " ? ";

    private Map<String, String> SQL_MAP = new HashMap<>();


    protected String cacheGetInsertSql(String tableName, List<String> columnLabels) {
        if (SQL_MAP.containsKey(tableName)) {
            return SQL_MAP.get(tableName);
        } else {
            SQL_MAP.put(tableName, genInsertSql(tableName, columnLabels));
            return cacheGetInsertSql(tableName, columnLabels);
        }
    }

    protected String genInsertSql(String tableName, List<String> columnLabels) {
        String writeDataSqlTemplate = new StringBuilder()
                .append("INSERT INTO ").append(tableName).append(" (").append(Joiner.on(",").join(columnLabels))
                .append(") VALUES(").append(Joiner.on(",").join(Collections2.transform(columnLabels, each -> SQL_PARAM_HOLDER)))
                .append(")")
                .append(onDuplicateKeyUpdateString(columnLabels))
                .toString();
        return writeDataSqlTemplate;
    }

    private String onDuplicateKeyUpdateString(List<String> columnHolders) {
        if (columnHolders == null || columnHolders.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ON DUPLICATE KEY UPDATE ");
        boolean first = true;
        for (String column : columnHolders) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(column);
            sb.append("=VALUES(");
            sb.append(column);
            sb.append(")");
        }

        return sb.toString();
    }

}
