package com.nchen.morphine.builders;

import com.nchen.morphine.annotations.Entity;
import org.junit.Test;
import org.reflections.Reflections;

import java.sql.SQLException;
import java.util.Set;

public class SchemaBuilderTests {

    @Test
    public void test() throws SQLException, ClassNotFoundException {
        Reflections reflections = new Reflections("com.nchen.morphine");
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        SchemaBuilder.build(entities);
    }
}
