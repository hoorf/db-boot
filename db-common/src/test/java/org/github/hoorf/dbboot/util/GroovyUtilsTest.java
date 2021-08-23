package org.github.hoorf.dbboot.util;

import groovy.util.Eval;
import org.apache.groovy.util.Maps;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class GroovyUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: eval(String express)
     */
    @Test
    public void testEval() throws Exception {
        GroovyUtils.eval("\"aaa${2..6}_as${0..3}_test_${0..5}\"");
    }

    @Test
    public void testEvalSingle() throws Exception {
        System.out.println(GroovyUtils.eval("\"aaa\""));
    }


    @Test
    public void testEvalParam() throws Exception {
        System.out.println(GroovyUtils.eval("\"aaa${id % 3}\"","id",4));
    }


    @Test
    public void testEvalParamMap() throws Exception {
        System.out.println(GroovyUtils.eval("\"aaa${id % 3}_b${number % 2}\"", Maps.of("id",22,"number",40)));
    }


    @Test
    public void testEvalParamMap2() throws Exception {
        System.out.println(GroovyUtils.eval("\"newDb_${id % 2}\"", Maps.of("id",40,"number",40)));
    }


} 
