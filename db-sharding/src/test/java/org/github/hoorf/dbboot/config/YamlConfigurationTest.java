package org.github.hoorf.dbboot.config;

import lombok.SneakyThrows;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertNotNull;

public class YamlConfigurationTest {


    @SneakyThrows
    @Test
    public void assertBuildConfig() {
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-sharding.yml"),
                        ShardingConfiguration.class);
        assertNotNull(configuration);
    }

    @SneakyThrows
    @Test
    public void assertDumpConfig() {
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-sharding.yml"),
                        ShardingConfiguration.class);
        String dump = yaml.dump(configuration);
        System.out.println(dump);

    }


}
