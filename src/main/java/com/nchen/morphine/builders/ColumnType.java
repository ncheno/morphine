package com.nchen.morphine.builders;

enum ColumnType {
    STRING("VARCHAR(%d)"), INT("INT(%d)"), INTEGER("INTEGER(%d)"), DOUBLE("DOUBLE");
    public static final int DEFAULT_INT_LENGTH = 11;
    public static final int DEFAULT_STRING_LENGTH = 255;

    private String sqlType;
    private int length = -1;

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public ColumnType setLength(int length) {
        this.length = length;
        return this;
    }

    public String getSqlType() {
        if(this == STRING)
            return String.format(sqlType, getLength(DEFAULT_STRING_LENGTH));
        if(this == INT || this == INTEGER)
            return String.format(sqlType, getLength(DEFAULT_INT_LENGTH));
        return sqlType;
    }

    private int getLength(int defaultLength) {
        return length <= 0 ? defaultLength : length;
    }
}
