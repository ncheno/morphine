package com.nchen.morphine.builders;

import com.nchen.morphine.entities.Machine;
import org.junit.Test;

import java.lang.reflect.Field;

public class ManyToManyBuilderTests {

    @Test
    public void test() throws NoSuchFieldException {
        Field field = Machine.class.getDeclaredField("drivers");
        TableMetaData table = new TableMetaData();
        table.name = "MACHINE";

        ManyToManyBuilder.build(table, field);
    }
}
