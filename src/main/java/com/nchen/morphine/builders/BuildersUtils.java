package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class BuildersUtils {

    public static String getColumnType(String javaType, int length) {
        return ColumnType.valueOf(javaType.toUpperCase())
                .setLength(length)
                .getSqlType();
    }

    public static String getColumnConstraints(Column column) {
        String constraints = "";
        constraints = !column.nullable() ? constraints + SQLConstants.NOT_NULL + " " : constraints;
        constraints = column.unique() ? constraints + SQLConstants.UNIQUE + " " : constraints;
        return constraints.trim();
    }

    public static String getIdConstraints() {
        return SQLConstants.NOT_NULL + " " + SQLConstants.AUTO_INCREMENT + " " + SQLConstants.PRIMARY_KEY;
    }

    public static String defaultColumnName(String str, String defaultStr) {
        return StringUtils.isNotEmpty(str) ? str : defaultStr;
    }

    public static Field findId(Class<?> entity) {
        List<Field> fields = Arrays.asList(entity.getDeclaredFields());
        return fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("Does not found primary key"));
    }

    public static boolean isForeignKeyExist(Field field) {
        return field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToOne.class);
    }
}
