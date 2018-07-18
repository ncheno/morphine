package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

import static com.nchen.morphine.builders.ForeignKeyBuilder.isForeignKeyExist;

class ColumnBuilder {

    private TableMetaData.ColumnMetaData columnMetaData;
    private Field columnData;

    static TableMetaData.ColumnMetaData build(TableMetaData.ColumnMetaData column, Field columnData) {
        if(column == null)
            throw new RuntimeException("Can`t create column without associated table");

        if(columnData == null)
            throw new RuntimeException("No information about column");

        ColumnBuilder columnBuilder = new ColumnBuilder(column, columnData);
        return columnBuilder.buildColumnData();
    }

    private ColumnBuilder(TableMetaData.ColumnMetaData columnMetaData, Field columnData) {
        this.columnMetaData = columnMetaData;
        this.columnData = columnData;
    }

    TableMetaData.ColumnMetaData buildColumnData() {
        if(isForeignKeyExist(columnData)) {
            createForeignKey();
        } else {
            if (columnData.isAnnotationPresent(Column.class)) {
                buildColumnDataFromAnnotation();
            } else {
                columnMetaData.name = columnData.getName();
                columnMetaData.type = BuildersUtils.getColumnType(columnData.getType().getSimpleName(), 0);
            }

            if (columnData.isAnnotationPresent(Id.class)) {
                columnMetaData.constraints += BuildersUtils.getIdConstraints();
            }
        }

        return columnMetaData;
    }

    void buildColumnDataFromAnnotation() {
        Column column = columnData.getAnnotation(Column.class);
        columnMetaData.name = StringUtils.defaultString(column.name(), columnData.getName());
        columnMetaData.constraints = BuildersUtils.getColumnConstraints(column);
        columnMetaData.type = BuildersUtils.getColumnType(columnData.getType().getSimpleName(), column.length());
    }

    private void createForeignKey() {
     /*   MorphineTable.ForeignKeyMorphineColumn foreignKeyMorphineColumn =
                getForeignKeyDefinition(morphineColumn.morphineTable.createForeignKey(), columnData);

        if(foreignKeyMorphineColumn != null) {
            morphineColumn.morphineTable.foreignKeyList.add(foreignKeyMorphineColumn);
            morphineColumn = foreignKeyMorphineColumn;
        } else {
            morphineColumn = null;
        }*/
    }
}
