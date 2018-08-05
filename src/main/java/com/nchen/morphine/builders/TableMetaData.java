package com.nchen.morphine.builders;

import java.util.*;

import static com.nchen.morphine.builders.SQLConstants.FOREIGN_KEY_STATEMENT;

public class TableMetaData {

    TableMetaData parent;
    String name;
    ColumnMetaData primaryKey;
    List<ColumnMetaData> columns = new ArrayList<>();
    Set<ForeignKeyColumnMetaData> foreignKeys = new HashSet<>();
    List<TableMetaData> joinTables = new ArrayList<>();

    ColumnMetaData createColumn() {
        return new ColumnMetaData(this);
    }

    ForeignKeyColumnMetaData createForeignKey() {
        return new ForeignKeyColumnMetaData(this);
    }

    void addForeignKey(ForeignKeyColumnMetaData foreignKeyColumnMetaData) {
        foreignKeys.add(foreignKeyColumnMetaData);
    }

    void addPrimaryKey(ColumnMetaData primaryKey) {
        this.primaryKey = primaryKey;
    }

    void addColumn(ColumnMetaData columnMetaData) {
        columns.add(columnMetaData);
    }

    void addJoinTable(TableMetaData relaton) { joinTables.add(relaton);}

    class ColumnMetaData {

        TableMetaData tableMetaData;
        String name;
        String type;
        String constraints = "";

        private ColumnMetaData(TableMetaData tableMetaData) {
            this.tableMetaData = tableMetaData;
        }

        String getSQL() {
            return name + " " + type + " " + constraints;
        }

        String getTableName() {
            return tableMetaData.name;
        }
    }

    class ForeignKeyColumnMetaData extends ColumnMetaData {

        String referencedTable;
        String referencedId;

        private ForeignKeyColumnMetaData(TableMetaData tableMetaData) {
            super(tableMetaData);
        }

        String getFKSQL() {
            return String.format(FOREIGN_KEY_STATEMENT, getForeignKeyName(), name, referencedTable, referencedId);
        }

        String getForeignKeyName() {
            return ("FK_" + getTableName() + "_" + referencedTable + "_" + name).toUpperCase();
        }
    }
}

