package com.grig.database.model;

public class AuthInfo {
    private Integer id;
    private String access_token;
    private Integer expires_id;
    private Integer user_id;

    public AuthInfo() {
    }

    public AuthInfo(Integer id, String access_token, Integer expires_id, Integer user_id) {
        this.id = id;
        this.access_token = access_token;
        this.expires_id = expires_id;
        this.user_id = user_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_id() {
        return expires_id;
    }

    public void setExpires_id(Integer expires_id) {
        this.expires_id = expires_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "id=" + id +
                ", access_token='" + access_token + '\'' +
                ", expires_id=" + expires_id +
                ", user_id=" + user_id +
                '}';
    }
}
