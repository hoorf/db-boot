package org.github.hoorf.dbboot.migrate.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalConfig {

    private Map<String, DataSourceConfig> dataSources = new HashMap<>();

    private DumpConfig dumpConfig;

    private ImportConfig importConfig;

    @Getter
    @Setter
    public static class DumpConfig {
        private Integer batchSize = 1000;
    }

    @Getter
    @Setter
    public static class ImportConfig {

    }
}
