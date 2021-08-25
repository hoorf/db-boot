package org.github.hoorf.dbboot.migrate.core.record;

import java.util.Arrays;
import java.util.Date;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataRecordTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testAddColumn() throws Exception {
    }


    @Test
    public void testGetColumn() throws Exception {
    }


    @Test
    public void testGetIdentityKey() throws Exception {
        DataRecord dataRecord = new DataRecord(null);
        dataRecord.setTableName("t_user");
        dataRecord.setPrimaryKeyValue(Arrays.asList("123",123,new Date(),new DataRecord(null)));
        System.out.println(dataRecord.getIdentityKey());

    }


} 
