package org.github.hoorf.dbboot.core.writer;

import org.github.hoorf.dbboot.core.ShardingContext;

public interface Writer extends Runnable {

    void init(ShardingContext shardingContext);


    boolean isFinished();
}
