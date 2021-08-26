package org.github.hoorf.dbboot.migrate.config;

import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

@Getter
@Setter
public class JobConfig {

    private String name;

    private String type;

    private String table;

    private String pk;

    private String source;

    private String target;

}
