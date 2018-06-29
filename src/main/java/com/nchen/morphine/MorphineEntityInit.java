package com.nchen.morphine;

import java.lang.reflect.Field;
import java.util.List;

public class MorphineEntityInit {
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

    public static void createTables(List<Class> entities) {
        for (Class entity : entities) {
            String createTableStatement =
                    CREATE_TABLE
                    + getTableName(entity) + " "
                    + getColumns(entity);
        }
    }

    private static String getTableName(Class entity) {
        String table = null;

        if(entity.isAnnotationPresent(Table.class)) {
            table = ((Table)entity.getAnnotation(Table.class)).value();
        } else {
            table = entity.getName();
        }

        return table.toUpperCase();
    }

    private static String getColumns(Class entity) {
        String cols = "";
        String primaryKey;

        Field fields[] = entity.getDeclaredFields();

        for(Field field : fields) {
            String column = null;

            column = getColumnDefinition(field);

            if(field.isAnnotationPresent(Id.class)) {
            }

        }

        return "(" + cols + ")";
    }

    private static String getColumnDefinition(Field field) {
        String name;
        String res = null;
        String notNull = "";

        name = field.getName();

        if(field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            name = column.name();

            if(!column.nullable()) {
                notNull = "NOT NULL ";
            }
        }



        return res;
    }

    private static String getNameInQuotes(String name) {
        if(name == null) {
            return "";
        }
        return "\'" + name + "\'";
    }

    private static class TableBuilder {
        private String name;
        private ColumnBuilder[] columns;
        private String primaryKey;

        private static class ColumnBuilder {
            private String name;
            private String type;
            private String constraints[];
        }
    }


}
