package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.Id;
import com.nchen.morphine.annotations.OneToOne;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ForeignKeyBuilder {

    public static TableMetaData.ForeignKeyColumnMetaData build() {
        return null;
    }

    static boolean isForeignKeyExist(Field field) {
        return false;
//        field.isAnnotationPresent(ManyToMany.class)
//                || field.isAnnotationPresent(ManyToOne.class)
//                || field.isAnnotationPresent(OneToMany.class)
//                || field.isAnnotationPresent(OneToOne.class);
    }

    private static TableMetaData.ForeignKeyColumnMetaData getForeignKeyDefinition(TableMetaData.ForeignKeyColumnMetaData foreignKeyColumnBuilder, Field referencedTable) {
        Class<?> foreignKeyClass = referencedTable.getType();

        if(foreignKeyClass.isAnnotationPresent(Entity.class)) {
            List<Field> fields = Arrays.asList(foreignKeyClass.getDeclaredFields());
            Field mappedByField = fields
                    .stream()
                    .filter(field -> field.isAnnotationPresent(OneToOne.class))
                    .findFirst()
                    .get();

            if(mappedByField == null || mappedByField.getAnnotation(OneToOne.class).mappedBy().isEmpty())
                return null; // foreign key is contained in other entity

            Field foreignKeyColumn =fields
                    .stream()
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .get();
            String foreignKeyName = getForeignKeyName(foreignKeyColumn);
            foreignKeyColumnBuilder.name = referencedTable.getName() + "_" + foreignKeyName;
            foreignKeyColumnBuilder.type = BuildersUtils.getColumnType(foreignKeyColumn.getType().getSimpleName(), 0);
            foreignKeyColumnBuilder.referencedId = foreignKeyName;
            foreignKeyColumnBuilder.referencedTable = referencedTable.getName();
            return foreignKeyColumnBuilder;
        } else {
            return null;
        }
    }

    public static String getForeignKeyName(Field foreignKeyColumn) {
        String foreignKeyName;

        if(foreignKeyColumn.isAnnotationPresent(Column.class)) {
            Column column = foreignKeyColumn.getAnnotation(Column.class);
            foreignKeyName = StringUtils.defaultString(column.name(), foreignKeyColumn.getName());
        } else {
            foreignKeyName = foreignKeyColumn.getName();
        }

        return foreignKeyName;
    }
}
