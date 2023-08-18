package com.vorono4ka.utilities;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ArrayUtils {
    public static <T> boolean contains(T[] array, T object) {
        return ArrayUtils.getIndexOf(array, object) != -1;
    }

    public static <T> int getIndexOf(T[] array, T object) {
        for (int i = 0; i < array.length; i++) {
            T item = array[i];
            if (item.equals(object)) {
                return i;
            }
        }

        return -1;
    }

    public static <T> String join(String listDelimiter, Collection<T> collection) {
        return join(listDelimiter, collection, String::valueOf);
    }

    public static <T> String join(String listDelimiter, Collection<T> collection, Function<T, String> toStringFunction) {
        return collection.stream().map(toStringFunction).collect(Collectors.joining(listDelimiter));
    }
}
