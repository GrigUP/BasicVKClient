package com.grig.model;

import com.grig.services.Token;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class Model {
    final private int application_id = 6672647;
    final private String redirect_uri = "https://oauth.vk.com/blank.html";
    final private String tokenFilePath = "src/main/resources/token_folder/token";
    final private String display = "page";
    final private String scope = "messages";
    final private String response_type = "token";
    final private double version = 5.8;

    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTokenFilePath() {
        return tokenFilePath;
    }

    private String getRequest(String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-length", "0");
            connection.setConnectTimeout(30000);

            System.out.println(connection.getURL());

            connection.connect();

            int responseCode = connection.getResponseCode();
            if(responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                br.close();

                return sb.toString();
            } else {
                System.out.println("Response code:" + responseCode);
            }

        } catch(Exception e) { e.printStackTrace(); }

        return null;
    }

    public String sendMessage(int id, String message) {
        String response = getRequest(getMessageUrl(id, message));
        System.out.println(response);
        return response;
    }

    public String getAccessTokenUrl() {
        return String.format("https://oauth.vk.com/authorize?client_id=%d&" +
                "display=%s&" +
                "redirect_uri=%s&" +
                "scope=%s&" +
                "response_type=%s&" +
                "v=%s&", application_id, display, redirect_uri, scope, response_type, version+"");
    }

    public String getMessageUrl(int id, String message) {
        Random random = new Random();
        return String.format("https://api.vk.com/method/messages.send?user_id=%d&" +
                "message=%s&" +
                "access_token=%s&" +
                "v=%s", id, message, token.getAccess_token(), version+"");
    }

    public String[] getSecureDataFromResponce(String response) {
        String parametersString = response.substring(response.indexOf("#")+1);

        String[] arrayParameters = new String[3];
        for (int i = 0; i < 3; i++) {
            int startIndex = 0;
            int finishIndex = 0;
            for (int j = 0; j < parametersString.length(); j++) {
                char ch = parametersString.charAt(j);
                if (ch == '=') {
                    startIndex = j+1;
                } else if (ch == '&') {
                    finishIndex = j;

                    arrayParameters[i] = parametersString.substring(startIndex, finishIndex);
                    StringBuilder stringBuilder = new StringBuilder(parametersString);
                    stringBuilder.delete(0, finishIndex+1);
                    parametersString = stringBuilder.toString();
                } else if (j == parametersString.length()-1) {
                    arrayParameters[i] = parametersString.substring(startIndex);
                }
            }
        }
        return arrayParameters;
    }
}
