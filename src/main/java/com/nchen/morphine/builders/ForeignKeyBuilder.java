package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.Id;
import com.nchen.morphine.annotations.OneToOne;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

import static com.nchen.morphine.builders.ColumnBuilder.getColumnType;

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
            Field[] fields = foreignKeyClass.getDeclaredFields();
            Field mappedByField = getFieldWithAnnotation(OneToOne.class, fields);

            if(mappedByField == null || mappedByField.getAnnotation(OneToOne.class).mappedBy().isEmpty())
                return null; // foreign key is contained in other entity

            Field foreignKeyColumn = getFieldWithAnnotation(Id.class, fields);
            String foreignKeyName;

            if(foreignKeyColumn.isAnnotationPresent(Column.class)) {
                Column column = foreignKeyColumn.getAnnotation(Column.class);
                foreignKeyName = StringUtils.defaultString(column.name(), foreignKeyColumn.getName());
            } else {
                foreignKeyName = foreignKeyColumn.getName();
            }

            foreignKeyColumnBuilder.name = referencedTable.getName() + "_" + foreignKeyName;
            foreignKeyColumnBuilder.type = getColumnType(foreignKeyColumn.getType().getSimpleName(), 0);
            foreignKeyColumnBuilder.referencedId = foreignKeyName;
            foreignKeyColumnBuilder.referencedTable = referencedTable.getName();
            return foreignKeyColumnBuilder;
        } else {
            return null;
        }
    }

    private static Field getFieldWithAnnotation(Class annotation, Field[] fields) {
        for(Field columnField : fields) {
            if(columnField.isAnnotationPresent(annotation)) {
                return columnField;
            }
        }

        return null;
    }
}
