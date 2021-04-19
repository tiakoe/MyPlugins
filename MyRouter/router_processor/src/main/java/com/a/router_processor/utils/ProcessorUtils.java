package com.a.router_processor.utils;

import java.util.Collection;

public class ProcessorUtils {

    public static boolean isEmptyString(String e) {
        return e == null || e.isEmpty();
    }

    public static boolean  isEmptyList(Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
