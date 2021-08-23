package org.github.hoorf.dbboot.core.writer;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

public class AbstractWriterTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: genInsertSql(String tableName, List<String> columnLabels)
     */
    @Test
    public void testGenInsertSql() throws Exception {
        AbstractWriter writer = new AbstractWriter() {
            @Override
            protected String genInsertSql(String tableName, List<String> columnLabels) {
                return super.genInsertSql(tableName, columnLabels);
            }
        };
        String sql = writer.genInsertSql("user", Arrays.asList("id", "name", "create_time"));
        System.out.println(sql);
    }


    /**
     * Method: onDuplicateKeyUpdateString(List<String> columnHolders)
     */
    @Test
    public void testOnDuplicateKeyUpdateString() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = AbstractWriter.getClass().getMethod("onDuplicateKeyUpdateString", List<String>.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
