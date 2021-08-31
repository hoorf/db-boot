package org.github.hoorf.dbboot.migrate.config;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalConfig {

    private Map<String, DataSourceConfig> dataSources = new HashMap<>();

    private DumpConfig dumpConfig = new DumpConfig();

    private ImportConfig importConfig = new ImportConfig();

    @Getter
    @Setter
    public static class DumpConfig {
        private Integer batchSize = 100000;
    }

    @Getter
    @Setter
    public static class ImportConfig {

    }
}
