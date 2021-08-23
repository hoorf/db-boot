package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShardingGlobalConfiguration {
    private Integer taskSize = 3;
}
