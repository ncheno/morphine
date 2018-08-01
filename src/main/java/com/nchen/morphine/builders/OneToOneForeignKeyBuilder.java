package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.OneToOne;

import java.lang.reflect.Field;

public class OneToOneForeignKeyBuilder extends ForeignKeyBuilderBase {

    public OneToOneForeignKeyBuilder(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData, Field referencedTableData) {
        super(foreignKeyMetaData, referencedTableData);
    }

    @Override
    boolean isMapped(Field mappedByField) {
        return mappedByField.isAnnotationPresent(OneToOne.class);
    }

    @Override
    String mappedBy(Field mappedByField) {
        return mappedByField.getAnnotation(OneToOne.class).mappedBy();
    }

    @Override
    String joinColumn() {
        return getReferencedTableData().getAnnotation(OneToOne.class).joinColumn();
    }

    @Override
    String getConstraints() {
        return SQLConstants.UNIQUE;
    }
}
