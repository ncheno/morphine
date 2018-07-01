package com.nchen.morphine;

public enum ColumnType {
    STRING("VARCHAR(255)"), INT("INT(11)"), DOUBLE("DOUBLE");
    private String sqlType;

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getSqlType() {
        return sqlType;
    }
}
