package com.grig.services;

public class Token {
    private String access_token;
    private long expires_in;
    private int user_id;

    public Token(String access_token, long expires_in, int user_id) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public long getUser_id() {
        return user_id;
    }
}
