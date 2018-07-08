package com.nchen.morphine;

import com.nchen.morphine.annotations.Entity;
import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class Morphine {
    private static Morphine morphine = null;
    private DbConfiguration dbConfiguration = null;
    private String scanPackage;

    private Morphine() {
        dbConfiguration = new DbConfiguration();
    }

    public static Morphine create() {
        if(morphine == null) {
            morphine = new Morphine();
        }
        return morphine;
    }

    public Morphine build() {
        try {
            dbConfiguration.init();
            Reflections reflections = new Reflections(scanPackage);
            Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
            MorphineEntityInit.createTables(entities);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void execute(String sql) {
        Statement statement = null;
        try {
            statement = dbConfiguration.connection.createStatement();
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

    public static Morphine getMorphine() {
        return morphine;
    }

    public Morphine setDbUrl(String url) {
        dbConfiguration.setUrl(url);
        return this;
    }

    public Morphine setDriverName(String driverName) {
        dbConfiguration.setDriverName(driverName);
        return this;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
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
