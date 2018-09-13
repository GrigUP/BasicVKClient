package com.grig.database.impl;

import com.grig.database.DataBaseFactory;
import com.grig.database.dao.AuthInfoDao;
import com.grig.database.model.AuthInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthInfoDaoImpl implements AuthInfoDao {
    private DataBaseFactory dataBaseFactory;

    public AuthInfoDaoImpl(DataBaseFactory dataBaseFactory) {
        this.dataBaseFactory = dataBaseFactory;
    }

    @Override
    public void create(AuthInfo newInstance) {
        String query = String.format("INSERT INTO users_info (user_id, access_token, expires_id) " +
                "VALUES (%d, \"%s\", %d);", newInstance.getUser_id(), newInstance.getAccess_token(), newInstance.getExpires_id());
        updateSQL(query);
    }

    @Override
    public List<AuthInfo> readAll() throws SQLException {
        String query = "SELECT * FROM users_info;";
        return readSQL(query);
    }

    @Override
    public AuthInfo readById(int id) {
        String query = String.format("SELECT * FROM users_info WHERE `id` = %d;", id);
        List<AuthInfo> dataList = readSQL(query);

        if (dataList.size() == 1) {
            return dataList.get(0);
        }
        return null;
    }

    @Override
    public AuthInfo readByUserId(int userId) {
        String query = String.format("SELECT * FROM users_info WHERE `user_id` = %d;", userId);
        List<AuthInfo> dataList = readSQL(query);

        if (dataList.size() == 1) {
            return dataList.get(0);
        }
        return null;
    }

    @Override
    public void update(AuthInfo updInst) {
        String query = String.format("UPDATE users_info " +
                "SET `id` = %d, `user_id` = %d, `access_token` = \"%s\", `expires_id` = %d " +
                "WHERE `id` = %d;", updInst.getId(), updInst.getUser_id(), updInst.getAccess_token(), updInst.getExpires_id(), updInst.getId());
        updateSQL(query);
    }

    @Override
    public void delete(AuthInfo delInst) {
        String query = String.format("DELETE FROM users_info WHERE `id` = %d", delInst.getId());
        updateSQL(query);
    }

    private List<AuthInfo> readSQL(String query) {
        List<AuthInfo> dataList = null;
        try(Connection connection = dataBaseFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery(query);

            dataList = new ArrayList<>();

            while (resultSet.next()) {
                AuthInfo authInfo = new AuthInfo();
                authInfo.setId(resultSet.getInt("id"));
                authInfo.setUser_id(resultSet.getInt("user_id"));
                authInfo.setAccess_token(resultSet.getString("access_token"));
                authInfo.setExpires_id(resultSet.getLong("expires_id"));

                dataList.add(authInfo);
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return dataList;
    }

    private void updateSQL(String query) {
        try(Connection connection = dataBaseFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
