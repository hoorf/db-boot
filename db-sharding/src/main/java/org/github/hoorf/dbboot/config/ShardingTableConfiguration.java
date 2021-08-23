package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public final class ShardingTableConfiguration {

    private ShardingTableSourceConfiguration source;

    private ShardingTableTargetConfiguration target;
}
