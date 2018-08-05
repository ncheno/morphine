package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class ColumnBuilderTests {

    private TableMetaData.ColumnMetaData columnMetaData;
    private Field columnData;

    private class TestEntity {
        @Id
        @Column
        int id;
        @Column
        String testField;
        @Column(name = "test_field", length = 100, nullable = false)
        String testFieldWithAnnotation;
    }

    @Before
    public void init() throws NoSuchFieldException {
        TableMetaData tableMetaData = new TableMetaData();
        columnMetaData = tableMetaData.createColumn();
        columnData = TestEntity.class.getDeclaredField("testField");
    }

    @Test(expected = RuntimeException.class)
    public void buildColumnDataWithNullColumnMetaDataTest() throws NoSuchFieldException {
        ColumnBuilder.build(null, columnData);
    }

    @Test(expected = RuntimeException.class)
    public void buildColumnDataWithNullColumnDataTest() throws NoSuchFieldException {
        ColumnBuilder.build(columnMetaData, null);
    }

    @Test
    public void buildColumnDataTest() throws NoSuchFieldException {
        TableMetaData.ColumnMetaData columnMetaDataRes = ColumnBuilder.build(columnMetaData, columnData);

        Assert.assertNotNull(columnMetaDataRes);
        Assert.assertEquals("testField", columnMetaDataRes.name);
        Assert.assertEquals("VARCHAR(255)", columnMetaDataRes.type);
        Assert.assertEquals(StringUtils.EMPTY, columnMetaDataRes.constraints);
    }

    @Test
    public void buildColumnDataTestWithColumnAnnotation() throws Exception {
        columnData =  TestEntity.class.getDeclaredField("testFieldWithAnnotation");
        TableMetaData.ColumnMetaData columnMetaDataRes = ColumnBuilder.build(columnMetaData, columnData);

        Assert.assertNotNull(columnMetaDataRes);
        Assert.assertEquals("test_field", columnMetaDataRes.name);
        Assert.assertEquals("VARCHAR(100)", columnMetaDataRes.type);
        Assert.assertEquals("NOT NULL", columnMetaDataRes.constraints);
    }

    @Test
    public void buildColumnDataTestWithIdAnnotation() throws Exception {
        columnData =  TestEntity.class.getDeclaredField("id");
        TableMetaData.ColumnMetaData columnMetaDataRes = ColumnBuilder.build(columnMetaData, columnData);

        Assert.assertNotNull(columnMetaDataRes);
        Assert.assertEquals("id", columnMetaDataRes.name);
        Assert.assertEquals("INT(11)", columnMetaDataRes.type);
        Assert.assertEquals("NOT NULL AUTO_INCREMENT PRIMARY KEY", columnMetaDataRes.constraints);
    }
}
