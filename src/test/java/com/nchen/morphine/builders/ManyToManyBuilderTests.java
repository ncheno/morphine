package com.nchen.morphine.builders;

import com.nchen.morphine.entities.Driver;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ManyToManyBuilderTests {

    @Test
    public void test() throws NoSuchFieldException {
        Field field = Driver.class.getDeclaredField("machines");
        TableMetaData table = new TableMetaData();
        table.name = "DRIVER";

        TableMetaData joinTable = ManyToManyBuilder.build(table, field);
        assertNotNull(joinTable);
        assertEquals(null, joinTable.primaryKey);
        assertEquals("driver_machine", joinTable.name);
        assertEquals(0, joinTable.joinTables.size());
        assertEquals(2, joinTable.foreignKeys.size());
    }
}
