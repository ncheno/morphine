package com.nchen.morphine;

import java.lang.reflect.Field;
import java.util.Set;

public class MorphineEntityInit {
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS";
    private final static String AUTO_INCREMENT = "AUTO_INCREMENT";
    private final static String NOT_NULL = "NOT NULL";
    private final static String PRIMARY_KEY = "PRIMARY KEY";
    private final static String UNIQUE = "UNIQUE";

    private static class TableBuilder {
        String name;
        ColumnBuilder[] columns;

        static TableBuilder build(Class<?> entity) {
            TableBuilder builder = new TableBuilder();
            builder.name = getTableName(entity);
            builder.columns = getColumns(entity);
            return builder;
        }

        void create() {
            String sql = createSql();
            Morphine.getMorphine().execute(sql);
        }

        String createSql() {
            String comm = CREATE_TABLE + " ";
            comm = comm + name + " ";
            comm = comm + "(";

            StringBuilder cols = new StringBuilder();
            for(ColumnBuilder col : columns) {
                cols.append(col.getQuery()).append(", ");
            }

            cols = new StringBuilder(cols.substring(0, cols.length() - 2));

            comm = comm + cols;
            String primaryKey = getPrimaryKeyColumn();

            if(primaryKey.length() > 0) {
                comm = comm + ", " + PRIMARY_KEY + "("+ primaryKey + ")";
            }
            comm = comm + ")";

            return comm;
        }

        private String getPrimaryKeyColumn() {
            for(TableBuilder.ColumnBuilder col : columns) {
                if(col.isPrimaryKey) {
                    return col.name;
                }
            }
            return "";
        }

        private static class ColumnBuilder {
            String name;
            String type;
            String constraints = "";
            boolean isPrimaryKey;

            String getQuery() {
                return name + " "
                        + type + " "
                        + constraints
                        + (isPrimaryKey ? NOT_NULL + " " + AUTO_INCREMENT : "");
            }
        }
    }


    public static void createTables(Set<Class<?>> entities) {
        for (Class<?> entity : entities) {
            TableBuilder tableBuilder = TableBuilder.build(entity);
            tableBuilder.create();
        }
    }

    private static String getTableName(Class<?> entity) {
        String table;

        if(entity.isAnnotationPresent(Table.class)) {
            table = entity.getAnnotation(Table.class).value();
        } else {
            table = entity.getSimpleName().toUpperCase();
        }

        return table.toUpperCase();
    }

    private static TableBuilder.ColumnBuilder[] getColumns(Class<?> entity) {
        Field fields[] = entity.getDeclaredFields();
        TableBuilder.ColumnBuilder[] columns = new TableBuilder.ColumnBuilder[fields.length];

        for(int i = 0; i < fields.length; i++) {
            columns[i] = getColumnDefinition(fields[i]);
        }

        return columns;
    }

    private static TableBuilder.ColumnBuilder getColumnDefinition(Field field) {
        TableBuilder.ColumnBuilder columnBuilder = new TableBuilder.ColumnBuilder();
        columnBuilder.name = field.getName().toUpperCase();
        columnBuilder.type = getColumnType(field.getType().getSimpleName());

        if(field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnBuilder.name = column.name().length() == 0 ? columnBuilder.name : column.name();
            columnBuilder.constraints = getColumnConstraints(column);
        }

        if(field.isAnnotationPresent(Id.class)) {
            columnBuilder.isPrimaryKey = true;
        }

        return columnBuilder;
    }

    private static String getColumnType(String type) {
        return ColumnType.valueOf(type.toUpperCase()).getSqlType();
    }

    private static String getColumnConstraints(Column column) {
        String constr = "";

        if(!column.nullable()) {
            constr = constr + NOT_NULL + " ";
        }

        if(column.unique()) {
            constr = constr + UNIQUE + " ";
        }

        return constr.trim();
    }

    private static String getNameInQuotes(String name) {
        if(name == null) {
            return "";
        }
        return "\'" + name + "\'";
    }
}
