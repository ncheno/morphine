package com.nchen.morphine.builders;

import com.nchen.morphine.entities.Driver;
import com.nchen.morphine.entities.Machine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
//TODO:
public class TableBuilderTests {

    private TableBuilder builder;

    @Before
    public void before() {
        builder = TableBuilder.build(Driver.class);
    }

    @Test
    public void testTableKeyNotNull() {
        TableMetaData table = builder.getTable();
        Assert.assertNotNull(table);
    }

    @Test
    public void testPrimaryKeyNotNull() {
        TableMetaData table = builder.getTable();
        Assert.assertNotNull(table.primaryKey);
    }
}
