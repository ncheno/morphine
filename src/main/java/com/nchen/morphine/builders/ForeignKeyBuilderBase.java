package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;

import java.lang.reflect.Field;

public abstract class ForeignKeyBuilderBase {
    private TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData;
    private Field referencedTableData;

    abstract boolean isMapped(Field mappedByField);

    abstract String mappedBy(Field mappedByField);

    abstract String joinColumn();

    abstract String getConstraints();

    public TableMetaData.ForeignKeyColumnMetaData buildForeignKeyData() throws NoSuchFieldException {
        Class<?> foreignKeyClass = referencedTableData.getType();

        if (!isMappedReferencedTable(foreignKeyClass)) {
            return null;
            //TODO:
//            throw new RuntimeException("Can`t find referenced table");
        }

        foreignKeyMetaData.name = joinColumn();
        foreignKeyMetaData.constraints = getConstraints();
        foreignKeyMetaData.referencedTable = referencedTableData.getName().toUpperCase();
        setReferencedIdAndTable(foreignKeyClass);
        return foreignKeyMetaData;
    }

    public Field getReferencedTableData() {
        return referencedTableData;
    }

    protected ForeignKeyBuilderBase(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData, Field referencedTableData) {
        this.foreignKeyMetaData = foreignKeyMetaData;
        this.referencedTableData = referencedTableData;
    }

    private void setReferencedIdAndTable(Class<?> foreignKeyClass) {
        Field id = BuildersUtils.findId(foreignKeyClass);
        foreignKeyMetaData.type = BuildersUtils.getColumnType(id.getType().getSimpleName(), ColumnType.DEFAULT_INT_LENGTH);
        foreignKeyMetaData.referencedId = BuildersUtils.defaultColumnName(id.getAnnotation(Column.class).name(), id.getName());
    }

    private boolean isMappedReferencedTable(Class<?> foreignKeyClass) throws NoSuchFieldException {
        String mappedTable = foreignKeyMetaData.tableMetaData.name.toLowerCase();
        Field mappedByField = foreignKeyClass.getDeclaredField(mappedTable);
        return isMapped(mappedByField) && mappedBy(mappedByField).equals(referencedTableData.getName());
    }
}
