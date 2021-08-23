package org.github.hoorf.dbboot.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;

@UtilityClass
@Slf4j
public class RangeSpiltUtils {

    //以显示字符为基准做编码
    private static final Integer CODE_BEGIN = 32;
    private static final Integer CODE_END = 126;

    private static final Integer CODE_RANGE = 84;


    public static Object[] spilt(Object min, Object max, int expectSlice) {
        if (min instanceof Integer) {
            return spilt(BigInteger.valueOf((Integer) min), BigInteger.valueOf((Integer) max), expectSlice);
        }
        if (min instanceof Long) {
            return spilt(BigInteger.valueOf((Long) min), BigInteger.valueOf((Long) max), expectSlice);
        } else if (min instanceof String) {
            return spilt((String) min, (String) max, expectSlice);
        }
        return null;
    }

    public static String[] spilt(String min, String max, int expectSlice) {
        BigInteger[] spilt = spilt(stringToBigInteger(min), stringToBigInteger(max), expectSlice);
        String[] result = new String[spilt.length];
        for (int i = 0; i < spilt.length; i++) {
            result[i] = bigIntegerToString(spilt[i]);
        }
        log.debug("{}", Arrays.asList(result));
        return result;
    }

    public static Long[] spilt(Long min, Long max, int expectSlice) {
        BigInteger[] spilt = spilt(BigInteger.valueOf(min), BigInteger.valueOf(max), expectSlice);
        Long[] result = new Long[spilt.length];
        for (int i = 0; i < spilt.length; i++) {
            result[i] = spilt[i].longValue();
        }
        return result;
    }


    public static BigInteger[] spilt(BigInteger min, BigInteger max, int expectSlice) {
        BigInteger[] result = new BigInteger[expectSlice + 1];
        if (min.compareTo(max) == 0) {
            return new BigInteger[]{min, max};
        }
        BigInteger range = max.subtract(min);
        BigInteger preRange = range.divide(BigInteger.valueOf(expectSlice));
        BigInteger start = min;
        for (int index = 0; index <= expectSlice; index++) {
            result[index] = start;
            start = start.add(preRange).add(BigInteger.ONE);
        }
        return result;
    }


    public BigInteger stringToBigInteger(String str) {
        BigInteger result = BigInteger.valueOf(0);
        int k = 0;
        for (int index = str.length() - 1; index >= 0; index--) {
            int c = str.charAt(index) - CODE_BEGIN;
            result = result.add(BigInteger.valueOf(c).multiply(BigInteger.valueOf(CODE_RANGE).pow(k)));
            k++;
        }
        return result;
    }

    public String bigIntegerToString(BigInteger bigInteger) {
        List<Integer> list = new ArrayList<>();
        BigInteger radixBigInteger = BigInteger.valueOf(CODE_RANGE);
        do {
            BigInteger remainder = bigInteger.remainder(radixBigInteger);
            list.add(remainder.intValue() + CODE_BEGIN);
            bigInteger = bigInteger.divide(radixBigInteger);
        } while (bigInteger.compareTo(BigInteger.ZERO) > 0);
        Collections.reverse(list);
        Map<Integer, Character> characterMap = new HashMap<>();
        for (int i = CODE_BEGIN; i <= CODE_END; i++) {
            characterMap.put(i, (char) i);
        }
        StringBuilder resultStringBuilder = new StringBuilder();
        list.forEach(each -> resultStringBuilder.append(characterMap.get(each)));
        return resultStringBuilder.toString();
    }
}
