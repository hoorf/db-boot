package org.github.hoorf.dbboot.migrate.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MigrateConfig {

    private GlobalConfig globalConfig;

    private Map<String,JobConfig> jobs = new HashMap<>();
}
