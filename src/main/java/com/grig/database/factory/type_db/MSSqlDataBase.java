package com.grig.database.factory.type_db;

import com.grig.database.factory.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSSqlDataBase implements DataBase {
    private final String DRIVER = null;
    private final String URL = null;
    private final String USER = null;
    private final String PASS = null;

    public MSSqlDataBase() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
