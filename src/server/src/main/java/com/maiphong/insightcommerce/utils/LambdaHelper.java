package com.maiphong.insightcommerce.utils;

import java.util.function.Function;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class LambdaHelper {
    public static <T> String nameOf(Function<T, ?> getter) {
        String methodName = getter.toString();
        return methodName.substring(methodName.indexOf("::") + 2);
    }
}
