package com.nchen.morphine;

import org.reflections.Reflections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class Morphine {
    private static Morphine morphine = null;
    private Configuration configuration = null;
    private String scanPackage;
    private static final String JDBC = "jdbc:mysql://";

    private Morphine() {
        configuration = new Configuration();
    }

    public static Morphine create() {
        if(morphine == null) {
            morphine = new Morphine();
        }
        return morphine;
    }

    public Morphine build() {
        try {
            configuration.init();
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
            statement = configuration.connection.createStatement();
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
        configuration.setUrl(url);
        return this;
    }

    public Morphine setDriverName(String driverName) {
        configuration.setDriverName(driverName);
        return this;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    private class Configuration {
        String DRIVER_NAME = "com.mysql.jdbc.Driver";
        String url = null;
        Connection connection = null;
        String user = null;
        String password = null;

        void init() throws ClassNotFoundException, SQLException {
           Class.forName(DRIVER_NAME);
           connection = DriverManager.getConnection(JDBC + url);
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
