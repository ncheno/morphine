package com.nchen.morphine;

import com.nchen.morphine.annotations.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final static String FOREIGN_KEY_STATEMENT = "FOREIGN KEY %s(%s) REFERENCES %s(%s)";


    private static class TableBuilder {
        String name;
        List<ColumnBuilder> columns;
        List<ForeignKeyColumnBuilder> foreignKeyList = new ArrayList<>();

        static TableBuilder build(Class<?> entity) {
            TableBuilder builder = new TableBuilder();
            builder.name = getTableName(entity);
            builder.columns = builder.getColumns(entity);
            return builder;
        }

        void create() {
            String sql = createSql(name, columns, foreignKeyList);
            Morphine.getMorphine().execute(sql);
        }

        private List<TableBuilder.ColumnBuilder> getColumns(Class<?> entity) {
            Field fields[] = entity.getDeclaredFields();
            List<TableBuilder.ColumnBuilder> columns = new ArrayList<>(fields.length);

            for(Field field : fields) {
                ColumnBuilder columnBuilder = createColumn();
                columns.add(getColumnDefinition(columnBuilder, field));
            }

            return columns;
        }

        ColumnBuilder createColumn() {
            return new ColumnBuilder(this);
        }

        ForeignKeyColumnBuilder createForeignKey() {
            return new ForeignKeyColumnBuilder(this);
        }

        private class ColumnBuilder {
            TableBuilder tableBuilder;
            String name;
            String type;
            String constraints = "";

            ColumnBuilder(TableBuilder tableBuilder) {
                this.tableBuilder = tableBuilder;
            }

            String getQuery() {
                return name + " " + type + " " + constraints;
            }
        }

        private class ForeignKeyColumnBuilder extends ColumnBuilder {
            String referencedTable;
            String referencedId;

            ForeignKeyColumnBuilder(TableBuilder tableBuilder) {
                super(tableBuilder);
            }

            String getForeignKeySql() {
                return String.format(FOREIGN_KEY_STATEMENT, getForeignKeyName(), name, referencedTable, referencedId);
            }

            String getForeignKeyName() {
                return "fk_" + referencedTable;
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
                .filter(Objects::nonNull)
                .map(column -> column.getQuery())
                .collect(Collectors.toList());
        return String.join(", ", columnList);
    }

    private static String getForeignKeysSql(List<TableBuilder.ForeignKeyColumnBuilder> columns) {
        List<String> columnList = columns
                .stream()
                .map(column -> column.getForeignKeySql())
                .collect(Collectors.toList());
        return String.join(", ", columnList);
    }

    private static String createSql(String tableName, List<TableBuilder.ColumnBuilder> columns,
                                    List<TableBuilder.ForeignKeyColumnBuilder> foreignKeyList) {
        String columnsSql = getColumnsSql(columns);
        String foreignKeys = getForeignKeysSql(foreignKeyList);
        String sql = !StringUtils.isEmpty(foreignKeys) ? (columnsSql + ", " + foreignKeys) : columnsSql;
        return String.format(CREATE_TABLE_STATEMENT, tableName, sql);
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
        if(isForeignKeyExist(field)) {
            TableBuilder.ForeignKeyColumnBuilder foreignKeyColumnBuilder = getForeignKeyDefinition(columnBuilder.tableBuilder.createForeignKey(), field);

            if(foreignKeyColumnBuilder != null) {
                columnBuilder.tableBuilder.foreignKeyList.add(foreignKeyColumnBuilder);
                columnBuilder = foreignKeyColumnBuilder;
            } else {
                columnBuilder = null;
            }
        } else {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columnBuilder.name = StringUtils.defaultString(column.name(), field.getName());
                columnBuilder.constraints = getColumnConstraints(column);
                columnBuilder.type = getColumnType(field.getType().getSimpleName(), column.length());
            } else {
                columnBuilder.name = field.getName();
                columnBuilder.type = getColumnType(field.getType().getSimpleName(), 0);
            }

            if (field.isAnnotationPresent(Id.class)) {
                columnBuilder.constraints += NOT_NULL + " " + AUTO_INCREMENT + " " + PRIMARY_KEY;
            }
        }

        return columnBuilder;
    }

    private static TableBuilder.ForeignKeyColumnBuilder getForeignKeyDefinition(TableBuilder.ForeignKeyColumnBuilder foreignKeyColumnBuilder, Field referencedTable) {
        Class<?> foreignKeyClass = referencedTable.getType();

        if(foreignKeyClass.isAnnotationPresent(Entity.class)) {
            Field[] fields = foreignKeyClass.getDeclaredFields();
            Field mappedByField = getFieldWithAnnotation(OneToOne.class, fields);

            if(mappedByField == null || mappedByField.getAnnotation(OneToOne.class).mappedBy().isEmpty())
                return null; // foreign key is contained in other entity

            Field foreignKeyColumn  = getFieldWithAnnotation(Id.class, fields);
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

    private static boolean isForeignKeyExist(Field field) {
        return field.isAnnotationPresent(ManyToMany.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
                || field.isAnnotationPresent(OneToOne.class);
    }

    private static String getColumnType(String type, int length) {
        return ColumnType.valueOf(type.toUpperCase()).setLehgth(length).getSqlType();
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
