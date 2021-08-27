package org.github.hoorf.dbboot.migrate.core.position;

import com.google.common.base.Preconditions;
import lombok.Getter;

public class RangePosition implements MigratePosition<RangePosition> {

    @Getter
    private final long beginValue;

    @Getter
    private final long endValue;


    public RangePosition(long beginValue, long endValue) {
        this.beginValue = beginValue;
        this.endValue = endValue;
    }

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
