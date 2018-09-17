package com.grig.database.factory.impl;

import com.grig.database.DBType;
import com.grig.database.factory.DataBase;
import com.grig.database.factory.DataBaseFactory;
import com.grig.database.factory.type_db.MSSqlDataBase;
import com.grig.database.factory.type_db.MySqlDataBase;

import java.sql.Connection;

public class DataBaseFactoryImpl implements DataBaseFactory {
    DBType type;

    public DataBaseFactoryImpl(DBType type) {
        this.type = type;
    }

    @Override
    public Connection getConnection() {
        DataBase dataBase = null;
        switch (type) {
            case MY_SQL:
                dataBase = new MySqlDataBase();
                break;
            case MS_SQL:
                dataBase = new MSSqlDataBase();
                break;
        }

        return dataBase.getConnection();
    }
}
