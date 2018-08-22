package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.OneToOne;
import com.nchen.morphine.entities.Machine;
import com.nchen.morphine.entity.EntityId;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

public class ForeignKeyResolverTests {

    @Entity
    class SecondEntity extends EntityId {
        @OneToOne(mappedBy = "firstEntityNotMapped")
        private FirstEntity firstRelation;

        @OneToOne(mappedBy = "entity")
        private FirstEntity secondRelation;
    }

    class FirstEntity extends EntityId {
        @OneToOne(joinColumn = "entity_id")
        SecondEntity entity;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCorrectReferencedTable() throws NoSuchFieldException {
        TableMetaData table = new TableMetaData();
        table.name = "FIRSTENTITY";
        Field documents = FirstEntity.class.getDeclaredField("entity");
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyResolver.build(table.createForeignKey(), documents);
        Assert.assertNotNull(foreignKey);
        Assert.assertEquals("SECONDENTITY", foreignKey.referencedTable);
    }

    @Test
    public void testCascadeType() throws NoSuchFieldException {
        TableMetaData table = new TableMetaData();
        table.name = "MACHINE";
        Field documents = Machine.class.getDeclaredField("driver");
        TableMetaData.ForeignKeyColumnMetaData foreignKey =
                ForeignKeyResolver.build(table.createForeignKey(), documents);
        Assert.assertNotNull(foreignKey);
        Assert.assertEquals(CascadeType.CASCADE_ALL.getValue(), foreignKey.getCascade());
    }
}
