package org.github.hoorf.dbboot.core.reader;

import org.apache.commons.lang3.tuple.Pair;
import org.github.hoorf.dbboot.core.ShardingContext;

import java.lang.reflect.Type;

public interface Reader extends Runnable {

    void init(String sql, Pair<Object, Object> pkRange, ShardingContext shardingContext);

    boolean isFinished();
}
