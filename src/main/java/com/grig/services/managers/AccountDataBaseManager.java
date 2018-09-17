package com.grig.services.managers;

import com.grig.database.JDBC.dao.AuthInfoDao;
import com.grig.database.model.AuthInfo;

import java.util.Date;

public class AccountDataBaseManager {
    private AuthInfoDao authInfoDao;

    public AccountDataBaseManager(AuthInfoDao authInfoDao) {
        this.authInfoDao = authInfoDao;
    }

    // update
    public void updateInfo(AuthInfo updated) {
        authInfoDao.update(updated);
    }

    // read
    public AuthInfo getAuthInfoById(int user_id) {
        return authInfoDao.readByUserId(user_id);
    }

    // create
    public void insertAuthInfo(AuthInfo authInfo) {
        authInfoDao.create(authInfo);
    }
}
