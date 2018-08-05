package com.nchen.morphine.builders;

import com.nchen.morphine.MorphineManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.nchen.morphine.builders.SQLConstants.ADD_COLUMN;
import static com.nchen.morphine.builders.SQLConstants.ADD_FOREIGN_KEY;

public class SchemaBuilder {

    private List<TableBuilder> tableBuilders = new ArrayList<>();

    public static SchemaBuilder build(Set<Class<?>> entities) {
        List<TableBuilder> tableBuilders = new ArrayList<>();

        for (Class<?> entity : entities) {
            TableBuilder tableBuilder = TableBuilder.build(entity);
            tableBuilders.add(tableBuilder);
        }

        return new SchemaBuilder(tableBuilders).buildData();
    }

    private SchemaBuilder buildData() {
        createTables();
        createForeignKeys();
        createJoinTable();
        return this;
    }

    private SchemaBuilder(List<TableBuilder> tableBuilders) {
        this.tableBuilders = tableBuilders;
    }

    private void createTables() {
        tableBuilders.forEach(builder -> execute(builder.getSQL()));
    }

    private void createForeignKeys() {
        tableBuilders.forEach(builder -> builder.getTable()
                .foreignKeys.stream()
                .filter(Objects::nonNull)
                .forEach(foreignKey -> {
                    addNewColumn(foreignKey);
                    addNewFK(foreignKey);
                }));
    }

    private void createJoinTable() {
        tableBuilders.forEach(builder -> builder.getTable()
                .joinTables.stream()
                .filter(Objects::nonNull)
                .forEach(joinTable -> {
                    execute(TableBuilder.build(joinTable).getSQL());
                    joinTable.foreignKeys.forEach(this::addNewFK);
                }));
    }

    private void addNewColumn(TableMetaData.ColumnMetaData column) {
        String sql = String.format(ADD_COLUMN, column.getTableName(), column.getSQL());
        execute(sql);
    }

    private void addNewFK(TableMetaData.ForeignKeyColumnMetaData fk) {
        String sql = String.format(ADD_FOREIGN_KEY, fk.getTableName(), fk.getFKSQL());
        execute(sql);
    }

    private void execute(String sql) {
        System.out.println(sql);
        MorphineManager.getMorphine().execute(sql);
    }
}
