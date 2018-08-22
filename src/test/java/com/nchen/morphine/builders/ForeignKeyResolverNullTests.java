package com.nchen.morphine.builders;

import com.nchen.morphine.entities.Documents;
import com.nchen.morphine.entities.Driver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;

@RunWith(Parameterized.class)
public class ForeignKeyResolverNullTests {
    @Parameters
    public static Object[][] data() {
        return new Object[][] {
                {"DOCUMENTS", Documents.class, "driver"},
                {"DRIVER", Driver.class, "machine"}
        };
    }

    @Parameter
    public String tableName;

    @Parameter(1)
    public Class entity;

    @Parameter(2)
    public String fieldName;

    private TableMetaData table;

    @Before
    public void before() {
        table = new TableMetaData();
        table.name = tableName;
    }

    @Test
    public void test() throws NoSuchFieldException {
        Field documents = entity.getDeclaredField(fieldName);
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyResolver.build(table.createForeignKey(), documents);
        Assert.assertNull(foreignKey);
    }
}
