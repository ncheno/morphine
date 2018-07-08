package com.nchen.morphine;

enum ColumnType {
    STRING("VARCHAR(%d)"), INT("INT(%d)"), INTEGER("INTEGER(%d)"), DOUBLE("DOUBLE");
    private String sqlType;
    private int length = -1;

    ColumnType(String sqlType) {
        this.sqlType = sqlType;
    }

    public ColumnType setLehgth(int length) {
        this.length = length;
        return this;
    }

    public String getSqlType() {
        if(this == STRING)
            return String.format(sqlType, getLength(255));
        if(this == INT || this == INTEGER)
            return String.format(sqlType, getLength(11));
        return sqlType;
    }

    private int getLength(int defaultLength) {
        return length <= 0 ? defaultLength : length;
    }
}
