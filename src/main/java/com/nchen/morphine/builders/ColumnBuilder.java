package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

import static com.nchen.morphine.builders.ForeignKeyBuilder.isForeignKeyExist;

public class ColumnBuilder {

    private TableMetaData.ColumnMetaData columnMetaData;
    private Field columnData;

    static TableMetaData.ColumnMetaData build(TableMetaData.ColumnMetaData column, Field columnData) {
        ColumnBuilder columnBuilder = new ColumnBuilder(column, columnData);
        return columnBuilder.buildColumnData();
    }

    static String getColumnType(String type, int length) {
        return ColumnType.valueOf(type.toUpperCase())
                .setLehgth(length)
                .getSqlType();
    }

    private ColumnBuilder(TableMetaData.ColumnMetaData columnMetaData, Field columnData) {
        this.columnMetaData = columnMetaData;
        this.columnData = columnData;
    }

    private TableMetaData.ColumnMetaData buildColumnData() {
        if(isForeignKeyExist(columnData)) {
            createForeignKey();
        } else {
            if (columnData.isAnnotationPresent(Column.class)) {
                buildColumnDataFromAnnotation();
            } else {
                columnMetaData.name = columnData.getName();
                columnMetaData.type = getColumnType(columnData.getType().getSimpleName(), 0);
            }

            if (columnData.isAnnotationPresent(Id.class)) {
                columnMetaData.constraints += BuilderConstants.NOT_NULL + " " +
                        BuilderConstants.AUTO_INCREMENT + " " + BuilderConstants.PRIMARY_KEY;
            }
        }

        return columnMetaData;
    }

    private void buildColumnDataFromAnnotation() {
        Column column = columnData.getAnnotation(Column.class);
        columnMetaData.name = StringUtils.defaultString(column.name(), columnData.getName());
        columnMetaData.constraints = getColumnConstraints(column);
        columnMetaData.type = getColumnType(columnData.getType().getSimpleName(), column.length());
    }

    private String getColumnConstraints(Column column) {
        String constraints = "";
        constraints = !column.nullable() ? constraints + BuilderConstants.NOT_NULL + " " : constraints;
        constraints = column.unique() ? constraints + BuilderConstants.UNIQUE + " " : constraints;
        return constraints.trim();
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
