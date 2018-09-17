package com.grig.database.factory;

import java.sql.Connection;

public interface DataBaseFactory {

    /**
     * factory method
     */
    Connection getConnection();
}
