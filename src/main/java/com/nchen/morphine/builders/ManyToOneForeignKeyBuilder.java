package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.ManyToOne;
import com.nchen.morphine.annotations.OneToMany;

import java.lang.reflect.Field;
import java.util.*;

public class ManyToOneForeignKeyBuilder extends ForeignKeyBuilderBase {

    public ManyToOneForeignKeyBuilder(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData, Field referencedTableData) {
        super(foreignKeyMetaData, referencedTableData);
    }

    @Override
    public TableMetaData.ForeignKeyColumnMetaData buildForeignKeyData() throws NoSuchFieldException {
        if (!(referencedTableData.getType().isAssignableFrom(List.class)
                || referencedTableData.getType().isAssignableFrom(Set.class))) {
            throw new RuntimeException(referencedTableData.getName() + " has wrong mapping");
        }

        return super.buildForeignKeyData();
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

    @Override
    Class<?> getForeignKeyTableType() {
        return BuildersUtils.getGenericTypeOfCollection(referencedTableData);
    }

    @Override
    Set<CascadeType> getCascade() {
        CascadeType[] cascadeTypes = getReferencedTableData().getAnnotation(ManyToOne.class).cascade();
        Set<CascadeType> res = new HashSet<>();
        Collections.addAll(res, cascadeTypes);
        return res;
    }
}
