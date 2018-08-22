package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.JoinTable;
import com.nchen.morphine.annotations.ManyToMany;
import org.reflections.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ManyToManyBuilder {

    private TableMetaData tableMetaData;
    private Field referencedTableData;

    public static TableMetaData build(TableMetaData tableMetaData,
                                      Field referencedTableData) throws NoSuchFieldException {
        return new ManyToManyBuilder(tableMetaData, referencedTableData).buildData();
    }

    private ManyToManyBuilder(TableMetaData tableMetaData, Field referencedTableData) {
        this.tableMetaData = tableMetaData;
        this.referencedTableData = referencedTableData;
    }

    private TableMetaData buildData() throws NoSuchFieldException {

        if (!(referencedTableData.getType().isAssignableFrom(List.class)
                || referencedTableData.getType().isAssignableFrom(Set.class))) {
            throw new RuntimeException(referencedTableData.getName() + " has wrong mapping");
        }

        Class<?> referencedEntity = BuildersUtils.getGenericTypeOfCollection(referencedTableData);

        if (!BuildersUtils.isEntity(referencedEntity)) {
            throw new RuntimeException(referencedEntity.getSimpleName() + " is not entity");
        }

        if (!referencedTableData.isAnnotationPresent(JoinTable.class)) {
            return null;
        }

        TableMetaData table = new TableMetaData();

        if (isMappedBy(referencedEntity)) {
            JoinTable joinTable = referencedTableData.getDeclaredAnnotation(JoinTable.class);
            table.parent = tableMetaData;
            table.name = joinTable.name();
            createAndAddFKColumn(table, referencedTableData.getDeclaringClass(), joinTable.joinColumn());
            createAndAddFKColumn(table, referencedEntity, joinTable.inverseJoinColumn());
        }

        return table;
    }

    private void createAndAddFKColumn(TableMetaData table, Class<?> referencedTable,
                                                              String name) throws NoSuchFieldException {
        Field joinColumn = BuildersUtils.findId(referencedTable);
        TableMetaData.ForeignKeyColumnMetaData foreignKeyColumn = table.createForeignKey();
        foreignKeyColumn.name = name;
        foreignKeyColumn.type = BuildersUtils.getColumnType(joinColumn.getType(), 0);
        foreignKeyColumn.constraints = SQLConstants.NOT_NULL;
        foreignKeyColumn.referencedId = joinColumn.getName();
        foreignKeyColumn.referencedTable = referencedTable.getSimpleName().toUpperCase();
        CascadeType[] cascadeTypes = referencedTableData.getDeclaredAnnotation(JoinTable.class).cascade();
        Collections.addAll(foreignKeyColumn.cascadeTypes, cascadeTypes);
        table.addForeignKey(foreignKeyColumn);
        table.addColumn(foreignKeyColumn);
    }

    private boolean isMappedBy(Class<?> referencedEntity) {
        return ReflectionUtils.getFields(referencedEntity)
                .stream()
                .filter(field -> !field.isAnnotationPresent(JoinTable.class)
                        && field.isAnnotationPresent(ManyToMany.class))
                .filter(field -> field.getDeclaredAnnotation(ManyToMany.class).mappedBy()
                        .equals(referencedTableData.getName()))
                .findFirst() != null;
    }
}
