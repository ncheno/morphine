package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableBuilder {

    private TableMetaData table;
    private Class<?> tableMetaInfo;

    public TableMetaData getTable() {
        return table;
    }

    public String getSql() {
        String columnsSql = getColumnsSql(table.columns);
        // String foreignKeys = getForeignKeysSql(foreignKeyList);
        // String sql = !StringUtils.isEmpty(foreignKeys) ? (columnsSql + ", " + foreignKeys) : columnsSql;
        return String.format(BuilderConstants.CREATE_TABLE_STATEMENT, table.name, columnsSql);
    }

    public static TableBuilder build(Class<?> tableMetaData) {
        TableBuilder tableBuilder = new TableBuilder(tableMetaData);
        tableBuilder.buildTable();
        return tableBuilder;
    }

    private String getColumnsSql(List<TableMetaData.ColumnMetaData> columns) {
        List<String> columnList = columns
                .stream()
                .filter(Objects::nonNull)
                .map(TableMetaData.ColumnMetaData::getQuery)
                .collect(Collectors.toList());
        return String.join(", ", columnList);
    }

    private TableBuilder(Class<?> tableMetaInfo) {
        this.table = new TableMetaData();
        this.tableMetaInfo = tableMetaInfo;
    }

    private TableMetaData buildTable() {
        table.name = getTableName();
        table.columns = getColumns();
        return table;
    }

    private String getTableName() {
        String table;

        if (tableMetaInfo.isAnnotationPresent(Table.class)) {
            table = tableMetaInfo.getAnnotation(Table.class).value();
        } else {
            table = tableMetaInfo.getSimpleName().toUpperCase();
        }

        return table.toUpperCase();
    }

    private List<TableMetaData.ColumnMetaData> getColumns() {
        Field fields[] = tableMetaInfo.getDeclaredFields();
        List<TableMetaData.ColumnMetaData> columns = new ArrayList<>(fields.length);

        for (Field field : fields) {
            TableMetaData.ColumnMetaData columnMetaData = ColumnBuilder.build(table.createColumn(), field);
            columns.add(columnMetaData);
        }

        return columns;
    }
}
