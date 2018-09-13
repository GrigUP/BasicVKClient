package com.grig.services.managers;

import com.grig.database.dao.AuthInfoDao;
import com.grig.database.model.AuthInfo;

import java.util.Date;

public class AccountDataBaseManager {
    private AuthInfoDao authInfoDao;

    public AccountDataBaseManager(AuthInfoDao authInfoDao) {
        this.authInfoDao = authInfoDao;
    }

    public boolean checkTokenAliveByUserId(int user_id) {
        AuthInfo authInfo = authInfoDao.readByUserId(user_id);

        Long currentUnixTime = new Date().getTime();

        if (authInfo != null) {
            Long tokenExpiresUnixTime = authInfo.getExpires_id();

            return tokenExpiresUnixTime > currentUnixTime;
        }

        return false;
    }

    public boolean isExists(int user_id) {
        AuthInfo dbAuthInfo = authInfoDao.readByUserId(user_id);

        return dbAuthInfo != null;
    }

    public void updateInfo(AuthInfo updated) {
        authInfoDao.update(updated);
    }

    public AuthInfo getAuthInfoById(int user_id) {
        return authInfoDao.readByUserId(user_id);
    }

    public void insertAuthInfo(AuthInfo authInfo) {
        authInfoDao.create(authInfo);
    }
}
