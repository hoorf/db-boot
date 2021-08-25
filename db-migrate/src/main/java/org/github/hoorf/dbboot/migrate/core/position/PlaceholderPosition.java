package org.github.hoorf.dbboot.migrate.core.position;

public class PlaceholderPosition implements MigratePosition<PlaceholderPosition> {
    @Override
    public int compareTo(PlaceholderPosition o) {
        return 1;
    }
}
