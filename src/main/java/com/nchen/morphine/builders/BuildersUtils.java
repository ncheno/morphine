package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

public class BuildersUtils {

    public static String getColumnType(Class<?> javaType, int length) {
        return ColumnType.valueOf(javaType.getSimpleName().toUpperCase())
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
        return ReflectionUtils.getAllFields(entity).stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("Does not found primary key"));
    }

    public static boolean isForeignKeyExist(Field field) {
        return field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToOne.class);
    }

    public static Class<?> getGenericTypeOfCollection(Field field) {
        ParameterizedType listType = (ParameterizedType) field.getGenericType();
        return (Class<?>) listType.getActualTypeArguments()[0];
    }

    public static boolean isEntity(Class<?> entity) {
        return entity.isAnnotationPresent(Entity.class);
    }

    public static List<Field> getFieldsByType(Class<?> entity, Class<?> type) {
        return ReflectionUtils.getAllFields(entity).stream()
                .filter(field -> field.getType().isAssignableFrom(type))
                .collect(Collectors.toList());
    }

}
