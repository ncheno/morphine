package com.nchen.morphine.builders;

import com.google.common.base.Predicate;
import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.JoinTable;
import com.nchen.morphine.annotations.ManyToMany;
import com.nchen.morphine.annotations.Table;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableBuilder {

    private TableMetaData table;
    private Class<?> tableMetaInfo;

    public TableMetaData getTable() {
        return table;
    }

    public String getSQL() {
        String columnsSQL = getColumnsSQL(table.columns);
        return String.format(SQLConstants.CREATE_TABLE_STATEMENT, table.name, columnsSQL);
    }

    public static TableBuilder build(TableMetaData tableMetaData) {
        return new TableBuilder(tableMetaData);
    }

    public static TableBuilder build(Class<?> tableMetaData) {
        TableBuilder tableBuilder = new TableBuilder(tableMetaData);
        tableBuilder.buildTable();
        return tableBuilder;
    }

    public String getColumnsSQL(List<TableMetaData.ColumnMetaData> columns) {
        List<String> columnList = columns
                .stream()
                .filter(Objects::nonNull)
                .map(TableMetaData.ColumnMetaData::getSQL)
                .collect(Collectors.toList());
        return String.join(", ", columnList);
    }

    private TableBuilder(Class<?> tableMetaInfo) {
        this.table = new TableMetaData();
        this.tableMetaInfo = tableMetaInfo;
    }

    private TableBuilder(TableMetaData tableMetaData) {
        this.table = tableMetaData;
    }

    private TableMetaData buildTable() {
        addTableName();
        addColumns();
        addForeignKeys();
        addJoinTables();
        return table;
    }

    private void addTableName() {
        String name = tableMetaInfo.isAnnotationPresent(Table.class)
                ? tableMetaInfo.getAnnotation(Table.class).value()
                : tableMetaInfo.getSimpleName();
        table.name = name.toUpperCase();
    }

    private void addColumns() {
        ReflectionUtils.getAllFields(tableMetaInfo,
                (Predicate<Field>) field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> {
                    TableMetaData.ColumnMetaData columnMetaData;
                    try {
                        columnMetaData = ColumnBuilder.build(table.createColumn(), field);
                        table.addColumn(columnMetaData);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void addForeignKeys() {
        List<Field> fields = Arrays.asList(tableMetaInfo.getDeclaredFields());
        fields.stream()
                .filter(BuildersUtils::isForeignKeyExist)
                .forEach(field -> {
                    TableMetaData.ForeignKeyColumnMetaData foreignKeyColumnMetaData;
                    try {
                        foreignKeyColumnMetaData = ForeignKeyResolver.build(table.createForeignKey(), field);
                        table.addForeignKey(foreignKeyColumnMetaData);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void addJoinTables() {
        ReflectionUtils.getAllFields(tableMetaInfo,
                (Predicate<Field>) field -> field.isAnnotationPresent(ManyToMany.class)
                        && field.isAnnotationPresent(JoinTable.class))
                .forEach(field -> {
                    TableMetaData joinTable;
                    try {
                        joinTable = ManyToManyBuilder.build(table, field);
                        table.addJoinTable(joinTable);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
    }
}
