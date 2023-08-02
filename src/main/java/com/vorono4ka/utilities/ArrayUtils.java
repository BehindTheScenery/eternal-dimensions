package com.vorono4ka.utilities;

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
}
