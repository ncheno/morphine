package com.nchen.morphine.builders;

import com.google.common.base.Preconditions;
import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;

import java.lang.reflect.Field;

class ColumnBuilder {

    private TableMetaData.ColumnMetaData columnMetaData;
    private Field columnData;

    static TableMetaData.ColumnMetaData build(TableMetaData.ColumnMetaData column, Field columnData) throws NoSuchFieldException {
        Preconditions.checkNotNull(column, "Can`t create column without associated table");
        Preconditions.checkNotNull(columnData, "No information about column");
        ColumnBuilder columnBuilder = new ColumnBuilder(column, columnData);
        return columnBuilder.buildColumnData();
    }

    private ColumnBuilder(TableMetaData.ColumnMetaData columnMetaData, Field columnData) {
        this.columnMetaData = columnMetaData;
        this.columnData = columnData;
    }

    private TableMetaData.ColumnMetaData buildColumnData() throws NoSuchFieldException {
        buildColumnDataFromAnnotation();

        if (columnData.isAnnotationPresent(Id.class)) {
            columnMetaData.constraints += BuildersUtils.getIdConstraints();
            columnMetaData.tableMetaData.addPrimaryKey(columnMetaData);
        }

        return columnMetaData;
    }

    void buildColumnDataFromAnnotation() {
        if (!columnData.isAnnotationPresent(Column.class))
            throw new RuntimeException("Field '" + columnData.getName() + "' in the table '"
                    + columnMetaData.tableMetaData.name + "' should be annotated by @Column annotation");

        Column column = columnData.getAnnotation(Column.class);
        columnMetaData.name = BuildersUtils.defaultColumnName(column.name(), columnData.getName());
        columnMetaData.constraints = BuildersUtils.getColumnConstraints(column);
        columnMetaData.type = BuildersUtils.getColumnType(columnData.getType(), column.length());
    }
}
