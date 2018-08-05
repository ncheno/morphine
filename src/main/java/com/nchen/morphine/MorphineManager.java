package com.nchen.morphine;

import com.google.common.base.Preconditions;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MorphineManager {
    private static Morphine morphine;

    public static void initMorphine(MysqlDataSource dataSource) {
        if(morphine == null) {
            morphine = Morphine.create(dataSource);
        }
    }

    public static Morphine getMorphine() {
        Preconditions.checkNotNull(morphine);
        return morphine;
    }
}
