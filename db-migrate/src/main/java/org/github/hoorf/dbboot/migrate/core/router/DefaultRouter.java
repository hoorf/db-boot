package org.github.hoorf.dbboot.migrate.core.router;

import org.github.hoorf.dbboot.migrate.core.record.DataRecord;

public class DefaultRouter implements Router {

    @Override
    public String getType() {
        return null;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String route(DataRecord dataRecord) {
        return null;
    }
}
