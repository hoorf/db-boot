package org.github.hoorf.dbboot.migrate.core;

import org.github.hoorf.dbboot.migrate.config.MigrateConfig;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.yaml.snakeyaml.Yaml;

public class MigrateBootstrapTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testStart() throws Exception {
        Yaml yaml = new Yaml();
        MigrateConfig configuration = yaml
            .loadAs(MigrateBootstrapTest.class.getClassLoader().getResourceAsStream("db-migrate.yml"),
                MigrateConfig.class);
        MigrateBootstrap bootstrap = new MigrateBootstrap();
        bootstrap.start(configuration);
    }


} 
