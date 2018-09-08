package com.grig.json.get_users_response_json;

public class ResponseJSON {
    int id;
    String first_name;
    String last_name;
    City city;
    String photo_50;
    boolean verified;

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public City getCity() {
        return city;
    }

    public String getPhoto_50() {
        return photo_50;
    }

    public boolean isVerified() {
        return verified;
    }

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
