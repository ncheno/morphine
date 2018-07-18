package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;

public class BuildersUtils {
    public static String getColumnType(String javaType, int length) {
        return ColumnType.valueOf(javaType.toUpperCase())
                .setLehgth(length)
                .getSqlType();
    }

    public static String getColumnConstraints(Column column) {
        String constraints = "";
        constraints = !column.nullable() ? constraints + BuilderConstants.NOT_NULL + " " : constraints;
        constraints = column.unique() ? constraints + BuilderConstants.UNIQUE + " " : constraints;
        return constraints.trim();
    }

    public static String getIdConstraints() {
        return BuilderConstants.NOT_NULL + " " + BuilderConstants.AUTO_INCREMENT + " " + BuilderConstants.PRIMARY_KEY;
    }
}
