package com.nchen.morphine;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.builders.SchemaBuilder;
import org.reflections.Reflections;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class Morphine {

    private DataSource dataSource = null;
    private String scanPackage;

    private Morphine(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Morphine create(DataSource dataSource) {
        return new Morphine(dataSource);
    }

    public Morphine build() throws ClassNotFoundException, SQLException {
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        SchemaBuilder.build(entities);
        return this;
    }

    public void execute(String sql) {
        Statement statement = null;
        try {
            statement = dataSource.getConnection().createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }
}
