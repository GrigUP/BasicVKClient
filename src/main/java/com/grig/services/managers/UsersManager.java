package com.grig.services.managers;

import com.google.gson.Gson;
import com.grig.json.get_users_response_json.JSONProfile;
import com.grig.json.get_users_response_json.ResponseJSON;
import com.grig.services.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersManager {

    public static Map<Integer, ResponseJSON> getUsersInfoByIds(List<Integer> ids, Token token) {
        Gson usersInfoResponse = new Gson();
        JSONProfile jsonProfiles = usersInfoResponse.fromJson(RequestManager.sendRequest(getUsersInfoUrl(ids, token)), JSONProfile.class);

        Map<Integer, ResponseJSON> result = new HashMap<>();
        for (ResponseJSON responseJSON: jsonProfiles.getResponse()) {
            result.put(responseJSON.getId(), responseJSON);
        }
        return result;
    }

    private static String getUsersInfoUrl(List<Integer> ids, Token token) {
        String result = "https://api.vk.com/method/users.get?user_ids=";

        for(int id:ids) {
            result += id + ",";
        }

        result = result.substring(0, result.length()-1);
        return result += "&access_token=" + token.getAccess_token() + "&v=5.8";
    }
}
