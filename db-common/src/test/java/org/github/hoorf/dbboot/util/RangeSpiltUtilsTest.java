package org.github.hoorf.dbboot.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Arrays;


public class RangeSpiltUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    /**
     * Method: spilt(BigInteger min, BigInteger max, int expectSlice)
     */
    @Test
    public void testSpilt() throws Exception {

        BigInteger[] spilt = RangeSpiltUtils.spilt(BigInteger.ONE, BigInteger.valueOf(100), 3);

        Assert.assertEquals(spilt[0], BigInteger.valueOf(1));
        Assert.assertEquals(spilt[1], BigInteger.valueOf(35));
        Assert.assertEquals(spilt[2], BigInteger.valueOf(69));
        Assert.assertEquals(spilt[3], BigInteger.valueOf(103));


        BigInteger[] spilt2 = RangeSpiltUtils.spilt(BigInteger.ONE, BigInteger.valueOf(99), 3);
        Assert.assertEquals(spilt2[0], BigInteger.valueOf(1));
        Assert.assertEquals(spilt2[1], BigInteger.valueOf(34));
        Assert.assertEquals(spilt2[2], BigInteger.valueOf(67));
        Assert.assertEquals(spilt2[3], BigInteger.valueOf(100));

        BigInteger[] spilt3 = RangeSpiltUtils.spilt(BigInteger.ONE, BigInteger.valueOf(101), 5);
        Assert.assertEquals(spilt3[0], BigInteger.valueOf(1));
        Assert.assertEquals(spilt3[1], BigInteger.valueOf(22));
        Assert.assertEquals(spilt3[2], BigInteger.valueOf(43));
        Assert.assertEquals(spilt3[3], BigInteger.valueOf(64));
        Assert.assertEquals(spilt3[4], BigInteger.valueOf(85));
        Assert.assertEquals(spilt3[5], BigInteger.valueOf(106));

    }

    /**
     * Method: stringToBigInteger(String str)
     */
    @Test
    public void testSpiltString() throws Exception {
        String[] spilt = RangeSpiltUtils.spilt("6119fca4bb0135cae0af6996", "6119fca4bb0135cae0af9996", 3);
        Arrays.asList(spilt).forEach(System.out::println);
    }

    /**
     * Method: bigIntegerToString(BigInteger bigInteger)
     */
    @Test
    public void testBigIntegerToString() throws Exception {
        Long[] spilt = RangeSpiltUtils.spilt(1427194098770644992L, 1427594098770644992L, 3);
        Arrays.asList(spilt).forEach(System.out::println);
    }

    @Test
    public void testSpiltA() {

        String min = "000cd3c863dd4d45ad77c32b44c8c7ee";
        String max = "fff4eb707a164da9a1ab547892f536ed";
        int exp = 2;
        Arrays.asList(RangeSpiltUtils.spilt(min, max, exp)).forEach(System.out::println);

    }

    @Test
    public void testToBigInteger() {

        String min = "000cd3c863dd4d45ad77c32b44c8c7ee";
        String max = "fff4eb707a164da9a1ab547892f536ee";
        int exp = 2;
        BigInteger minInt = RangeSpiltUtils.stringToBigInteger(min);
        System.out.println("min : " + minInt);
        BigInteger maxInt = RangeSpiltUtils.stringToBigInteger(max);
        System.out.println("max : " + maxInt);
        BigInteger midInt = maxInt.add(minInt).divide(BigInteger.valueOf(2));
        System.out.println("mid : " + midInt);
        System.out.println("til : " + maxInt.add(minInt));
        System.out.println("tol : " + midInt.multiply(BigInteger.valueOf(2)));
        System.out.println(RangeSpiltUtils.bigIntegerToString(midInt));

    }

    @Test
    public void testToBigIntegerSecond() {

        String min = "abc12ee3ccf";
        String max = "abd12ee3aad";
        int exp = 2;
        BigInteger minInt = RangeSpiltUtils.stringToBigInteger(min);
        System.out.println("min : " + minInt);
        BigInteger maxInt = RangeSpiltUtils.stringToBigInteger(max);
        System.out.println("max : " + maxInt);
        BigInteger midInt = maxInt.add(minInt).divide(BigInteger.valueOf(2));
        System.out.println("mid : " + midInt);
        System.out.println("til : " + maxInt.add(minInt));
        System.out.println("tol : " + midInt.multiply(BigInteger.valueOf(2)));
        System.out.println(RangeSpiltUtils.bigIntegerToString(midInt));

    }

} 
