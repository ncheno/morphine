package com.nchen.morphine.builders;

import java.util.ArrayList;
import java.util.List;

public class TableMetaData {

    private final static String FOREIGN_KEY_STATEMENT = "FOREIGN KEY %s(%s) REFERENCES %s(%s)";

    String name;
    List<ColumnMetaData> columns;
    List<ForeignKeyColumnMetaData> foreignKeyList = new ArrayList<>();

    ColumnMetaData createColumn() {
        return new ColumnMetaData(this);
    }

    ForeignKeyColumnMetaData createForeignKey() {
        return new ForeignKeyColumnMetaData(this);
    }

    class ColumnMetaData {
        TableMetaData tableMetaData;
        String name;
        String type;
        String constraints = "";

        ColumnMetaData(TableMetaData tableMetaData) {
            this.tableMetaData = tableMetaData;
        }

        String getQuery() {
            return name + " " + type + " " + constraints;
        }
    }

    class ForeignKeyColumnMetaData extends ColumnMetaData {
        String referencedTable;
        String referencedId;

        ForeignKeyColumnMetaData(TableMetaData tableMetaData) {
            super(tableMetaData);
        }

        String getForeignKeySql() {
            return String.format(FOREIGN_KEY_STATEMENT, getForeignKeyName(), name, referencedTable, referencedId);
        }

        String getForeignKeyName() {
            return "fk_" + referencedTable;
        }
    }
}

