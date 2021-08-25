package org.github.hoorf.dbboot.migrate.core.position;

import com.google.common.base.Preconditions;

public class RangePosition implements MigratePosition<RangePosition> {

    private final long beginValue;

    private final long endValue;

    public RangePosition(String keys) {
        Preconditions.checkNotNull(keys);
        String[] split = keys.split(",");
        beginValue = new Long(split[0]);
        endValue = new Long(split[1]);
    }

    @Override
    public int compareTo(RangePosition rangePosition) {
        if (null == rangePosition) {
            return 0;
        }
        return Long.compare(rangePosition.beginValue, this.beginValue);
    }
}
