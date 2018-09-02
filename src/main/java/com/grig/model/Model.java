package com.grig.model;

import com.grig.services.MessageManager;
import com.grig.services.RequestManager;
import com.grig.services.Token;
import com.grig.services.TokenSaverAndChecker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;

import java.io.IOException;

public class Model {
    final private int application_id = 6672647;
    final private String redirect_uri = "https://oauth.vk.com/blank.html";
    final private String tokenFilePath = "src/main/resources/token_folder/token";
    final private String display = "page";
    final private String scope = "messages";
    final private String response_type = "token";
    final private double version = 5.8;

    private boolean isAuthenticated;
    private Token token;

    public Model() {
        String request = null;
        try {
            request = TokenSaverAndChecker.getTokenFromFile(getTokenFilePath());
        } catch (IOException e) {
            e.getStackTrace();
        }

        if (request.contains("access_token=") && request.contains("user_id=") && request.contains("expires_id=")) {
            String[] keys = getSecureDataFromResponce(request);
            token = new Token(keys[0], Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));
            isAuthenticated = true;
        } else  isAuthenticated = false;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private String getTokenFilePath() {
        return tokenFilePath;
    }

    public String sendMessage(int id, String message) {
        return MessageManager.sendMessage(id, message, token);
    }

    public String getConversations(int offset, int count) {
        return MessageManager.getMessageList(offset, count, token);
    }

    private String getAccessTokenUrl() {
        return String.format("https://oauth.vk.com/authorize?client_id=%d&" +
                "display=%s&" +
                "redirect_uri=%s&" +
                "scope=%s&" +
                "response_type=%s&" +
                "v=%s&", application_id, display, redirect_uri, scope, response_type, version+"");
    }

    private String[] getSecureDataFromResponce(String response) {
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

    public void getAuthenticateDialog() {
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        final Stage tempStage = new Stage();
        tempStage.setScene(new Scene(webView));
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED && webView.getEngine().getDocument().getDocumentURI().contains("#access_token=")) {
                    String url = webView.getEngine().getDocument().getDocumentURI();
                    String[] keys = getSecureDataFromResponce(url);
                    token = new Token(keys[0], Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));
                    tempStage.close();

                    try {
                        TokenSaverAndChecker.writeTokenToFile(getTokenFilePath(), token);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        webEngine.load(getAccessTokenUrl());
        tempStage.showAndWait();
    }


}
