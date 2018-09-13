package com.grig.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseFactory {
    private final String DRIVER;
    private final String URL;
    private final String USER_NAME;
    private final String PASSWORD;

    public DataBaseFactory(String DRIVER, String URL, String USER_NAME, String PASSWORD) {
        this.DRIVER = DRIVER;
        this.URL = URL;
        this.USER_NAME = USER_NAME;
        this.PASSWORD = PASSWORD;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
