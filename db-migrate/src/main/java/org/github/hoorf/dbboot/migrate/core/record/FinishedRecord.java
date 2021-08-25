package org.github.hoorf.dbboot.migrate.core.record;

import org.github.hoorf.dbboot.migrate.core.position.MigratePosition;

public class FinishedRecord extends Record {
    public FinishedRecord(MigratePosition<?> position) {
        super(position);
    }
}
