package com.nchen.morphine.builders;

public interface SQLConstants {
    String AUTO_INCREMENT = "AUTO_INCREMENT";
    String NOT_NULL = "NOT NULL";
    String PRIMARY_KEY = "PRIMARY KEY";
    String UNIQUE = "UNIQUE";
    String CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS %s (%s)";
    String FOREIGN_KEY_STATEMENT = "FOREIGN KEY %s(%s) REFERENCES %s(%s)";
    String ADD_FOREIGN_KEY = "ALTER TABLE %s ADD %s";
    String ADD_COLUMN = "ALTER TABLE %s ADD COLUMN %s";


}
