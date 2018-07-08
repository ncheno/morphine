package com.nchen.morphine;

import com.nchen.morphine.annotations.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MorphineEntityInit {
    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS";
    private final static String AUTO_INCREMENT = "AUTO_INCREMENT";
    private final static String NOT_NULL = "NOT NULL";
    private final static String PRIMARY_KEY = "PRIMARY KEY";
    private final static String UNIQUE = "UNIQUE";
    private final static String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS %s (%s)";
    private final static String PRIMARY_KEY_STATEMENT = ", PRIMARY KEY (%s)";

    private static class TableBuilder {
        String name;
        List<ColumnBuilder> columns;
        List<String> foreignKeyList = new ArrayList<>();

        static TableBuilder build(Class<?> entity) {
            TableBuilder builder = new TableBuilder();
            builder.name = getTableName(entity);
            builder.columns = builder.getColumns(entity);
            return builder;
        }

        void create() {
            String sql = createSql(name, columns);
            Morphine.getMorphine().execute(sql);
        }

        private List<TableBuilder.ColumnBuilder> getColumns(Class<?> entity) {
            Field fields[] = entity.getDeclaredFields();
            List<TableBuilder.ColumnBuilder> columns = new ArrayList<>(fields.length);

            for(Field field : fields) {
                ColumnBuilder columnBuilder = new ColumnBuilder(this);
                columns.add(getColumnDefinition(columnBuilder, field));
            }

            return columns;
        }

        private class ColumnBuilder {
            TableBuilder tableBuilder;
            String name;
            String type;
            String constraints = "";
            boolean isForeignKey;

            ColumnBuilder(TableBuilder tableBuilder) {
                this.tableBuilder = tableBuilder;
            }

            String getQuery() {
                return name + " " + type + " " + constraints;
            }
        }
    }

    public static void createTables(Set<Class<?>> entities) {
        for (Class<?> entity : entities) {
            TableBuilder tableBuilder = TableBuilder.build(entity);
            tableBuilder.create();
        }
    }

    private static String getColumnsSql(List<TableBuilder.ColumnBuilder> columns) {
        List<String> columnList = columns
                .stream()
                .map(column -> column.getQuery())
                .collect(Collectors.toList());
        return String.join(", ", columnList);
    }

    private static String createSql(String tableName, List<TableBuilder.ColumnBuilder> columns) {
        String columnsSql = getColumnsSql(columns);
        return String.format(CREATE_TABLE_STATEMENT, tableName, columnsSql);
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


    private static TableBuilder.ColumnBuilder getColumnDefinition(TableBuilder.ColumnBuilder columnBuilder, Field field) {

        if(field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnBuilder.name =  StringUtils.defaultString(column.name(), field.getName());
            columnBuilder.constraints = getColumnConstraints(column);
            columnBuilder.type = getColumnType(field.getType().getSimpleName(), column.length());
        } else {
            columnBuilder.name = field.getName();
            columnBuilder.type = getColumnType(field.getType().getSimpleName(), 0);
        }

        if(field.isAnnotationPresent(Id.class)) {
            columnBuilder.constraints += NOT_NULL + " " + AUTO_INCREMENT + " " + PRIMARY_KEY;
        }

        if(isForeignKeyExist(field)) {
            columnBuilder.isForeignKey = true;
            getForeignKeyDefinition(field);
        }

        return columnBuilder;
    }

    private static String getForeignKeyDefinition(Field field) {
        Class<?> foreignKeyClass = field.getType();

        if(foreignKeyClass.isAnnotationPresent(Entity.class)) {
            Field[] fields = foreignKeyClass.getDeclaredFields();
            String foreignKeyName = null;
            Field foreignKeyColumn = null;

            for(Field columnField : fields) {
                if(columnField.isAnnotationPresent(Id.class)) {
                    foreignKeyColumn = columnField;
                    break;
                }
            }

            if(foreignKeyColumn.isAnnotationPresent(Column.class)) {
                Column column = foreignKeyColumn.getAnnotation(Column.class);
                foreignKeyName = StringUtils.defaultString(column.name(), field.getName());
            } else {
                foreignKeyName = field.getName();
            }

        }
        return null;
    }

    private static boolean isForeignKeyExist(Field field) {
        return field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(OneToOne.class);
    }

    private static String getColumnType(String type, int length) {
        try {
            return ColumnType.valueOf(type.toUpperCase()).setLehgth(length).getSqlType();
        } catch (IllegalArgumentException e) {
            return type;
        }
    }

    private static String getColumnConstraints(Column column) {
        String constraints = "";

        if(!column.nullable()) {
            constraints = constraints + NOT_NULL + " ";
        }

        if(column.unique()) {
            constraints = constraints + UNIQUE + " ";
        }

        return constraints.trim();
    }
}
