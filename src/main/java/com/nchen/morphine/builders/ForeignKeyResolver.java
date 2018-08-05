package com.nchen.morphine.builders;

import com.google.common.base.Preconditions;
import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.ManyToOne;
import com.nchen.morphine.annotations.OneToOne;

import java.lang.reflect.Field;

final public class ForeignKeyResolver {

    public static TableMetaData.ForeignKeyColumnMetaData build(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData,
                                                               Field referencedTableData) throws NoSuchFieldException {
        Preconditions.checkNotNull(foreignKeyMetaData, "Can`t create foreign key without associated table");
        Preconditions.checkNotNull(referencedTableData, "No information about foreign key");
        return buildForeignKeyData(foreignKeyMetaData, referencedTableData);
    }

    private static TableMetaData.ForeignKeyColumnMetaData buildForeignKeyData(TableMetaData.ForeignKeyColumnMetaData foreignKeyMetaData,
                                                                       Field referencedTableData) throws NoSuchFieldException {
        Class<?> foreignKeyClass = referencedTableData.getType();
        ForeignKeyBuilderBase foreignKeyBuilderBase;

        if (!foreignKeyClass.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException(foreignKeyClass.getSimpleName() + " is not Entity");
        } else {
            if (referencedTableData.isAnnotationPresent(OneToOne.class)) {
                foreignKeyBuilderBase = new OneToOneForeignKeyBuilder(foreignKeyMetaData, referencedTableData);
                return foreignKeyBuilderBase.buildForeignKeyData();
            } else if (referencedTableData.isAnnotationPresent(ManyToOne.class)) {
                foreignKeyBuilderBase = new ManyToOneForeignKeyBuilder(foreignKeyMetaData, referencedTableData);
                return foreignKeyBuilderBase.buildForeignKeyData();
            }
        }

        return null;
    }
}
