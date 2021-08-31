package org.github.hoorf.dbboot.migrate.config;

import java.util.List;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobConfig {

    private String type;

    private SourceConfig source;

    private TargetConfig target;


    @Getter
    @Setter
    public static class SourceConfig {
        private String pk;

        private String table;

        private String dataSourceName;

        private DumpConfig dumpConfig = new DumpConfig();

        @Getter
        @Setter
        public static class DumpConfig {
            private Integer batchSize = 100000;

            private String databaseType;
        }
    }

    @Getter
    @Setter
    public static class TargetConfig {
        private String table;

        private String dataSourceName;

        private TableStrategyConfiguration tableStrategy ;

        private DataSourceStrategyConfiguration dataSourceStrategy ;

        private ImportConfig importConfig = new ImportConfig();

        @Getter
        @Setter
        public static class ImportConfig {
            private Integer batchSize = 100000;
            private String databaseType;
        }
    }

    @Getter
    @Setter
    public static final class TableStrategyConfiguration extends RouterConfiguration {

    }

    @Getter
    @Setter
    public static final class DataSourceStrategyConfiguration extends RouterConfiguration {

    }

    @Getter
    @Setter
    public static class RouterConfiguration {
        protected String type;

        protected Properties props;
    }




}
