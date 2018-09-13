package com.grig.database.dao;

import com.grig.database.model.AuthInfo;

import java.sql.SQLException;
import java.util.List;

public interface AuthInfoDao {

    //create
    void create(AuthInfo newInstance);

    //read
    List<AuthInfo> readAll() throws SQLException;

    AuthInfo readById(int id);

    AuthInfo readByUserId(int userId);

    //update
    void update(AuthInfo updateInstance);

    //delete
    void delete(AuthInfo deleteInstance);

}
