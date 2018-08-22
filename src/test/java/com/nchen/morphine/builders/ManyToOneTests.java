package com.nchen.morphine.builders;

import com.nchen.morphine.TestUtils;
import com.nchen.morphine.entities.Driver;
import com.nchen.morphine.entities.Machine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;

@RunWith(Parameterized.class)
public class ManyToOneTests {

    @Parameters
    public static Object[][] data() {
        return new Object[][] {
                {"DRIVER", Driver.class, "documents", "DOCUMENTS", "referencedTable"},
                {"DRIVER", Driver.class, "documents", "documents_id", "name"},
                {"DRIVER", Driver.class, "documents", "id", "referencedId"},
                {"MACHINE", Machine.class, "driver", "DRIVER", "referencedTable"},
                {"MACHINE", Machine.class, "driver", "driver_id", "name"},
                {"MACHINE", Machine.class, "driver", "id", "referencedId"}
        };
    }

    @Parameter
    public String tableName;

    @Parameter(1)
    public Class entity;

    @Parameter(2)
    public String fieldName;

    @Parameter(3)
    public String expected;

    @Parameter(4)
    public String fieldWithActualResult;

    private TableMetaData table;

    @Before
    public void before() {
        table = new TableMetaData();
        table.name = tableName;
    }

    @Test
    public void testOneToOneRelationReferencedTable() throws Exception {
        Field documents = entity.getDeclaredField(fieldName);
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyResolver.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        String result = (String) TestUtils.findField(foreignKey.getClass(), fieldWithActualResult).get(foreignKey);
        Assert.assertEquals(expected, result);
    }
}
