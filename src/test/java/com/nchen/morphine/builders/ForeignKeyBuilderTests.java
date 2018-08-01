package com.nchen.morphine.builders;

import com.nchen.morphine.entities.Documents;
import com.nchen.morphine.entities.Driver;
import com.nchen.morphine.entities.Machine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class ForeignKeyBuilderTests {

    private TableMetaData table;
    private static final String MACHINE = "MACHINE";
    private static final String DRIVER = "DRIVER";
    private static final String DOCUMENTS = "DOCUMENTS";

    @Before
    public void before() {
        table = new TableMetaData();
    }

    @Test
    public void testOneToOneRelationReferencedTable() throws NoSuchFieldException {
        table.name = DRIVER;
        Field documents = Driver.class.getDeclaredField(DOCUMENTS.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals(DOCUMENTS, foreignKey.referencedTable);
    }

    @Test
    public void testOneToOneRelationJoinColumnName() throws NoSuchFieldException {
        table.name = DRIVER;
        Field documents = Driver.class.getDeclaredField(DOCUMENTS.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals("documents_id", foreignKey.name);
    }

    @Test
    public void testOneToOneRelationReferencedId() throws NoSuchFieldException {
        table.name = DRIVER;
        Field documents = Driver.class.getDeclaredField(DOCUMENTS.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals("docId", foreignKey.referencedId);
    }

    @Test
    public void testManyToOneRelationReferencedTable() throws NoSuchFieldException {
        table.name = MACHINE;
        Field documents = Machine.class.getDeclaredField(DRIVER.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals(DRIVER, foreignKey.referencedTable);
    }

    @Test
    public void testManyToOneRelationJoinColumnName() throws NoSuchFieldException {
        table.name = MACHINE;
        Field documents = Machine.class.getDeclaredField(DRIVER.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals("driver_id", foreignKey.name);
    }

    @Test
    public void testManyToOneRelationReferencedId() throws NoSuchFieldException {
        table.name = MACHINE;
        Field documents = Machine.class.getDeclaredField(DRIVER.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNotNull(foreignKey);
        Assert.assertEquals("dId", foreignKey.referencedId);
    }

    @Test
    public void testNullOneToOneRelation() throws NoSuchFieldException {
        table.name = DOCUMENTS;
        Field documents = Documents.class.getDeclaredField(DRIVER.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNull(foreignKey);
    }

    @Test
    public void testNullOneToManyRelation() throws NoSuchFieldException {
        table.name = DRIVER;
        Field documents = Driver.class.getDeclaredField(MACHINE.toLowerCase());
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyBuilder.build(table.createForeignKey(), documents);

        Assert.assertNull(foreignKey);
    }
}
