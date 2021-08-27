package org.github.hoorf.dbboot.migrate.core.dumper;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;

public class DumperLoaderTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testGetDumper() throws Exception {
        Dumper mysql = DumperFactory.getInstance("mysql");
        assertTrue(mysql instanceof MysqlDumper);
        assertTrue(DumperFactory.getInstance("oracle") instanceof OracleDumper);
    }


} 
