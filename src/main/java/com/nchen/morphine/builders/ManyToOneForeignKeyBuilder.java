package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.ManyToOne;
import com.nchen.morphine.annotations.OneToMany;

import java.lang.reflect.Field;

public class ManyToOneForeignKeyBuilder extends ForeignKeyBuilderBase {

    public ManyToOneForeignKeyBuilder(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData, Field referencedTableData) {
        super(foreignKeyMetaData, referencedTableData);
    }

    @Override
    boolean isMapped(Field mappedByField) {
        return mappedByField.isAnnotationPresent(OneToMany.class);
    }

    @Override
    String mappedBy(Field mappedByField) {
        return mappedByField.getAnnotation(OneToMany.class).mappedBy();
    }

    @Override
    String joinColumn() {
        return getReferencedTableData().getAnnotation(ManyToOne.class).joinColumn();
    }

    @Override
    String getConstraints() {
        return "";
    }
}
