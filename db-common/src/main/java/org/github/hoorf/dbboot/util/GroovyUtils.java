package org.github.hoorf.dbboot.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import groovy.lang.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@UtilityClass
public class GroovyUtils {

    private static final GroovyShell SHELL = new GroovyShell();

    public static List<String> eval(String express) {
        express = prepare(express);
        List<String> result = new ArrayList<>();
        Script script = SHELL.parse(express);
        Object object = script.run();
        if (object instanceof GString) {
            result.addAll(getGStringResult((GString) object));
        } else {
            result.add(object.toString());
        }
        return result;
    }

    public static synchronized String eval(String express, String paramName, Object param) {
        SHELL.setVariable(paramName, param);
        Object result = SHELL.evaluate(prepare(express));
        SHELL.removeVariable(paramName);
        return result.toString();
    }

    public static synchronized String eval(String express, Map<String, Object> params) {
        params.entrySet().forEach(each -> SHELL.setVariable(each.getKey(), each.getValue()));
        Object result = SHELL.evaluate(prepare(express));
        params.keySet().forEach(each -> SHELL.removeVariable(each));
        return result.toString();
    }


    private static String prepare(String express) {
        express = express.startsWith("\"") ? express : "\"" + express;
        express = express.endsWith("\"") ? express : express + "\"";
        return express;
    }

    private static Collection<? extends String> getGStringResult(GString object) {
        Set<String> result = new HashSet<>();
        String template = Joiner.on("%s").join(object.getStrings());
        List<Set<String>> cartesianList = Lists.transform(Arrays.asList(object.getValues()), each -> Sets.newHashSet(Collections2.transform((Collection) each, Object::toString)));
        Set<List<String>> lists = Sets.cartesianProduct(cartesianList);
        lists.forEach(each -> result.add(String.format(template, each.toArray(new String[each.size()]))));
        return result;
    }

}
