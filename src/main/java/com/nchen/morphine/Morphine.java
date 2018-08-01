package com.nchen.morphine;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.builders.TableBuilder;
import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class Morphine {

    private DbConfiguration dbConfig = null;
    private String scanPackage;

    private Morphine() {
        dbConfig = new DbConfiguration();
    }

    public static Morphine create() {
        return new Morphine();
    }

    public Morphine build() throws ClassNotFoundException, SQLException {
        dbConfig.init();
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
        createTables(entities);
        return this;
    }

    public void execute(String sql) {
        Statement statement = null;
        try {
            statement = dbConfig.connection.createStatement();
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

    public Morphine setDbUrl(String url) {
        dbConfig.setUrl(url);
        return this;
    }

    public Morphine setDriverName(String driverName) {
        dbConfig.setDriverName(driverName);
        return this;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    private void createTables(Set<Class<?>> entities) {
        for (Class<?> entity : entities) {
            TableBuilder tableBuilder = TableBuilder.build(entity);
            execute(tableBuilder.getSQL());
        }
    }

    private class DbConfiguration {
        String DRIVER_NAME = "com.mysql.jdbc.Driver";
        String url = null;
        Connection connection = null;
        String user = null;
        String password = null;

        void init() throws ClassNotFoundException, SQLException {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(url);
        }

        void setDriverName(String driverName) {
            DRIVER_NAME = driverName;
        }

        void setUrl(String url) {
            this.url = url;
        }

        public void setConnection(Connection connection) {
            this.connection = connection;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
