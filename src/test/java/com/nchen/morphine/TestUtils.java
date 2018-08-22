package com.nchen.morphine;

import java.lang.reflect.Field;

public class TestUtils {
    public static Field findField(Class<?> clazz, String fieldName) {
        Class<?> current = clazz;
        do {
            try {
                return current.getDeclaredField(fieldName);
            } catch(Exception e) {}
        } while((current = current.getSuperclass()) != null);
        return null;
    }
}
