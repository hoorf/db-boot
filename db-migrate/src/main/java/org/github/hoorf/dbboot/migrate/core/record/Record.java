package org.github.hoorf.dbboot.migrate.core.record;

import lombok.Getter;
import lombok.Setter;
import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

@Getter
@Setter
public abstract class Record {

    private final MigratePosition<?> position;

    private long commitTime;

    public Record(MigratePosition<?> position) {
        this.position = position;
    }
}
