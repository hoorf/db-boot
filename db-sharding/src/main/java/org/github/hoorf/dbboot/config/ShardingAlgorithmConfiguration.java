package org.github.hoorf.dbboot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Properties;

@Getter
@Setter
@ToString
public final class ShardingAlgorithmConfiguration {

    private String type;

    private Properties props;
}
