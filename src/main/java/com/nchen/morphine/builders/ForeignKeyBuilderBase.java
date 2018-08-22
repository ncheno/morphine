package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public abstract class ForeignKeyBuilderBase {
    private TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData;
    private Field referencedTableData;

    abstract boolean isMapped(Field mappedByField);

    abstract String mappedBy(Field mappedByField);

    abstract String joinColumn();

    abstract String getConstraints();

    abstract Set<CascadeType> getCascade();

    public TableMetaData.ForeignKeyColumnMetaData buildForeignKeyData() throws NoSuchFieldException {
        Class<?> foreignKeyClass = referencedTableData.getType();

        if (!isMappedReferencedTable(foreignKeyClass)) {
            return null;
        }

        foreignKeyMetaData.name = joinColumn();
        foreignKeyMetaData.constraints = getConstraints();
        foreignKeyMetaData.referencedTable = referencedTableData.getType().getSimpleName().toUpperCase();
        foreignKeyMetaData.cascadeTypes = getCascade();
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
        foreignKeyMetaData.type = BuildersUtils.getColumnType(id.getType(), ColumnType.DEFAULT_INT_LENGTH);
        foreignKeyMetaData.referencedId = BuildersUtils.defaultColumnName(id.getAnnotation(Column.class).name(), id.getName());
    }

    private boolean isMappedReferencedTable(Class<?> foreignKeyClass) throws NoSuchFieldException {
        List<Field> fields = BuildersUtils.getFieldsByType(foreignKeyClass, referencedTableData.getDeclaringClass());
        for (Field field : fields) {
            Field mappedByField = foreignKeyClass.getDeclaredField(field.getName());
            if (isMapped(mappedByField) && mappedBy(mappedByField).equals(referencedTableData.getName()))
                return true;
        }
        return false;
    }
}
