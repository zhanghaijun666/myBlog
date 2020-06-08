package com.blog.utils;

import org.javalite.common.Convert;

import java.util.ArrayList;
import java.util.List;

public class BasicConvertUtils {

    public static String toString(Object value, final String defaultValue) {
        String strVal = Convert.toString(value);
        return strVal == null ? defaultValue : strVal;
    }

    public static boolean toBoolean(Object value) {
        return Convert.toBoolean(value);
    }

    public static double toDouble(Object value, final double defaultValue) {
        try {
            return Convert.toDouble(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static float toFloat(Object value, final float defaultValue) {
        return value == null ? defaultValue : Convert.toFloat(value);
    }

    public static long toLong(Object value, final long defaultValue) {
        try {
            Long longVal = Convert.toLong(value);
            return longVal == null ? defaultValue : longVal;
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static short toShort(Object value, final short defaultValue) {
        try {
            return Convert.toShort(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static int toInteger(Object value, final Integer defaultValue) {
        try {
            return Convert.toInteger(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static List<Integer> toInteger(List<Object> list, int itemDefaultValue) {
        List<Integer> returnSet = new ArrayList();
        if (null != list && !list.isEmpty()) {
            for (Object obj : list) {
                returnSet.add(BasicConvertUtils.toInteger(obj, itemDefaultValue));
            }
        }
        return returnSet;
    }
}

