package com.grig.model;

import com.grig.database.model.AuthInfo;
import com.grig.json.get_users_response_json.ResponseJSON;
import com.grig.services.*;
import com.grig.services.managers.AccountDataBaseManager;
import com.grig.services.managers.MessageManager;
import com.grig.services.managers.UsersManager;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import javafx.stage.WindowEvent;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Model {
    final private int user_id = 21013306;
    final private int application_id = 6672647;
    final private String redirect_uri = "https://oauth.vk.com/blank.html";
    final private String tokenFilePath = "src/main/resources/token_folder/token";
    final private String display = "page";
    final private String scope = "messages";
    final private String response_type = "token";
    final private double version = 5.8;

    private boolean isAuthenticated;
    private Token token;
    private AccountDataBaseManager accountDataBaseManager;

    public Model(AccountDataBaseManager accountDataBaseManager) {
        this.accountDataBaseManager = accountDataBaseManager;
        getAuthorization();
    }

    public int getUser_id() {
        return user_id;
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

    public String getMessageHistory(int id, int offset, int count) {
        return MessageManager.getMessageHistory(id, offset, count, token);
    }

    public Map<Integer, ResponseJSON> getUsersInfoById(List<Integer> ids) {
        return UsersManager.getUsersInfoByIds(ids, token);
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
        tempStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });

        tempStage.setScene(new Scene(webView));
        tempStage.setHeight(400);
        tempStage.setWidth(660);
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED && webView.getEngine().getDocument().getDocumentURI().contains("#access_token=")) {
                    String url = webView.getEngine().getDocument().getDocumentURI();
                    String[] keys = getSecureDataFromResponce(url);

                    token = new Token(keys[0], Long.parseLong(keys[1]), Integer.parseInt(keys[2]));
                    AuthInfo authInfo = new AuthInfo(keys[0], Long.parseLong(keys[1]) + new Date().getTime(), Integer.parseInt(keys[2]));

                    if (!accountDataBaseManager.isExists(user_id)) {
                        accountDataBaseManager.insertAuthInfo(authInfo);
                    }

                    if (accountDataBaseManager.checkTokenAliveByUserId(user_id)) {
                        accountDataBaseManager.updateInfo(authInfo);
                    }

                    tempStage.close();
                }
            }
        });

        webEngine.load(getAccessTokenUrl());
        tempStage.showAndWait();
    }

    public void getAuthorization() {
        if (accountDataBaseManager.isExists(user_id) && !accountDataBaseManager.checkTokenAliveByUserId(user_id)) {
            System.out.println("Token find and alive!");
            AuthInfo authInfo = accountDataBaseManager.getAuthInfoById(user_id);
            token = new Token(authInfo.getAccess_token(), authInfo.getExpires_id(), authInfo.getUser_id());
        }
          else {
            System.out.println("Token is old or can't be find!");
            getAuthenticateDialog();
        }
    }
}
