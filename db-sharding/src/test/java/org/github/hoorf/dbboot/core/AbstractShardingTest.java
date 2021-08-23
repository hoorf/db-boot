package org.github.hoorf.dbboot.core;

import org.apache.commons.lang3.time.StopWatch;
import org.github.hoorf.dbboot.config.ShardingConfiguration;
import org.github.hoorf.dbboot.config.YamlConfigurationTest;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.yaml.snakeyaml.Yaml;

public class AbstractShardingTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: validateConfig()
     */
    @Test
    public void testValidateConfig() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: process()
     */
    @Test
    public void testProcess() throws Exception {
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-single.yml"),
                        ShardingConfiguration.class);
        AbstractSharding abstractSharding = new AbstractSharding(configuration);
        abstractSharding.process();
    }

    @Test
    public void testBigProcess() throws Exception {
        StopWatch stopWatch = new StopWatch();
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-bigsingle.yml"),
                        ShardingConfiguration.class);
        AbstractSharding abstractSharding = new AbstractSharding(configuration);
        stopWatch.start();
        abstractSharding.process();
        stopWatch.stop();
        System.out.println(stopWatch);
    }
    @Test
    public void testBigMultiProcess() throws Exception {
        StopWatch stopWatch = new StopWatch();
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-bigsingle-multi.yml"),
                        ShardingConfiguration.class);
        AbstractSharding abstractSharding = new AbstractSharding(configuration);
        stopWatch.start();
        abstractSharding.process();
        stopWatch.stop();
        System.out.println(stopWatch);
    }


    @Test
    public void testSharding() throws Exception {
        StopWatch stopWatch = new StopWatch();
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-sharding.yml"),
                        ShardingConfiguration.class);
        AbstractSharding abstractSharding = new AbstractSharding(configuration);
        stopWatch.start();
        abstractSharding.process();
        stopWatch.stop();
        System.out.println(stopWatch);
    }

    @Test
    public void testRound() throws Exception {
        StopWatch stopWatch = new StopWatch();
        Yaml yaml = new Yaml();
        ShardingConfiguration configuration = yaml
                .loadAs(YamlConfigurationTest.class.getClassLoader().getResourceAsStream("db-round.yml"),
                        ShardingConfiguration.class);
        AbstractSharding abstractSharding = new AbstractSharding(configuration);
        stopWatch.start();
        abstractSharding.process();
        stopWatch.stop();
        System.out.println(stopWatch);
    }


} 
