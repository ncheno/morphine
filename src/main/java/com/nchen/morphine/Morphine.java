package com.nchen.morphine;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Morphine {
    private static Morphine morphine = null;
    private Configuration configuration = null;

    public static Morphine create() {
        if(morphine == null) {
            morphine = new Morphine();
        }
        return morphine;
    }

    public Morphine build() throws IOException {
       // configuration.init();

        List<Class> entities = MorphineEntityScanner.scanPackageForEntities("com.nchen.morphine");

        return this;
    }

    private Morphine() {
        configuration = new Configuration();
    }

    public Morphine setDbUrl(String url) {
        configuration.setUrl(url);
        return this;
    }

    public Morphine setDriverName(String driverName) {
        Configuration.setDriverName(driverName);
        return this;
    }

    public static class Configuration {
        private static String DRIVER_NAME = "com.mysql.jdbc.Driver";
        private String url = null;
        private Connection connection = null;
        private String user = null;
        private String password = null;

        void init() {
            try {
                Class.forName(Configuration.DRIVER_NAME);
                connection = DriverManager.getConnection(url);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static String getDriverName() {
            return DRIVER_NAME;
        }

        static void setDriverName(String driverName) {
            DRIVER_NAME = driverName;
        }

        public String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        public Connection getConnection() {
            return connection;
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
