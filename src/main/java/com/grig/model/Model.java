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

    /**
     * id для пользователя, который использует приложение перманентно
     * (должен храниться где-то на устройстве).
     */
    final private int user_id = 21013306;

    /**
     * Настройки разрабатываемого приложения с https://vk.com/dev
     */
    final private int application_id = 6672647;
    final private String redirect_uri = "https://oauth.vk.com/blank.html";
    final private String display = "page";
    final private String scope = "messages";
    final private String response_type = "token";
    final private double version = 5.8;

    private Token token;
    private AccountDataBaseManager accountDataBaseManager;

    /**
     * При создании экземпляра класса модели происходит привязка, используемого для доступа
     * к базе данных экземпляра класса AccountDataBaseManager, и авторизация пользователя в приложении.
     * @param accountDataBaseManager
     */
    public Model(AccountDataBaseManager accountDataBaseManager) {
        this.accountDataBaseManager = accountDataBaseManager;
        getAuthorization();
    }

    public int getUser_id() {
        return user_id;
    }

    // Делигируем менеджеру сообщений запрос на отправку сообщения
    public String sendMessage(int id, String message) {
        return MessageManager.sendMessage(id, message, token);
    }

    // Делигируем менеджеру сообщений запрос на получения списка дивлогов
    public String getConversations(int offset, int count) {
        return MessageManager.getMessageList(offset, count, token);
    }

    // Делигируем менеджеру сообщений запрос на получение истории диалога с пользоавтелем id
    public String getMessageHistory(int id, int offset, int count) {
        return MessageManager.getMessageHistory(id, offset, count, token);
    }

    // Делигируем менеджеру профилей запрос на получение данных о пользователей по листу из id
    public Map<Integer, ResponseJSON> getUsersInfoByIds(List<Integer> ids) {
        return UsersManager.getUsersInfoByIds(ids, token);
    }

    // Вспомогательный метод по подготовке URL для получения токена доступа
    private String getAccessTokenUrl() {
        return String.format("https://oauth.vk.com/authorize?client_id=%d&" +
                "display=%s&" +
                "redirect_uri=%s&" +
                "scope=%s&" +
                "response_type=%s&" +
                "v=%s&", application_id, display, redirect_uri, scope, response_type, version+"");
    }

    // Парсинг URL-ответа с токеном от сервера VK, в результате массив строк
    // [токен, время действие токена, id пользователя]
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

    /**
     * В случае необходимости в получении нового токена доступа открывается панель WebView,
     * в которой необходиомо ввести данные для авторизации, после чего будет получен новый токен,
     * обновлен или добавлен в базу данных.
     */
    public void getAuthenticateDialog() {
        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        // В случае закрытия окна авторизации дальнейшее использование приложения бессмысленно
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

        /**
         * Добавление слушателя для WebView. В случае, если страница загружена и содержит в URL-строке
         * токен доступа, производится парсинг данной строки, извлечение требуеммых данных, и занесения их
         * в базу, либо обновления уже старых данных.
         */
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED && webView.getEngine().getDocument().getDocumentURI().contains("#access_token=")) {
                    String url = webView.getEngine().getDocument().getDocumentURI();
                    // парсинг
                    String[] keys = getSecureDataFromResponce(url);

                    // обновления токена
                    token = new Token(keys[0], Long.parseLong(keys[1])*1000 + new Date().getTime(), Integer.parseInt(keys[2]));

                    // обновление данных в базе
                    AuthInfo authInfo = new AuthInfo(keys[0], Long.parseLong(keys[1])*1000 + new Date().getTime(), Integer.parseInt(keys[2]));
                    AuthInfo dbAuthInfo = accountDataBaseManager.getAuthInfoById(user_id);
                    System.out.println(dbAuthInfo);
                    System.out.println(authInfo);
                    if (dbAuthInfo == null) {
                        accountDataBaseManager.insertAuthInfo(authInfo);
                    } else if (dbAuthInfo.getExpires_id() < authInfo.getExpires_id()) {
                        accountDataBaseManager.updateInfo(authInfo);
                    }

                    // закрытие панели WebView
                    tempStage.close();
                }
            }
        });

        webEngine.load(getAccessTokenUrl());
        tempStage.showAndWait();
    }

    /**
     * Метод по авторизации пользователя в приложении, в случае если срок действия токена
     * в базе данных не истек и он существует, предоставляется доступ к данным пользователя.
     */
    public void getAuthorization() {

        // загрузка авторизационных данных о пользователе с базы данных
        AuthInfo authInfo = accountDataBaseManager.getAuthInfoById(user_id);

        // существую ли данные
        if (authInfo == null) {
            System.out.println("User info by id:" + user_id + " not exists!");
            getAuthenticateDialog();
        }

        // не истек ли срок действия
        else if (authInfo.getExpires_id() < new Date().getTime()) {
            System.out.println("Access token is not alive!");
            getAuthenticateDialog();
        } else {

            // запись данных с бд для использования приложением
            token = new Token(authInfo.getAccess_token(), authInfo.getExpires_id(), authInfo.getUser_id());
            System.out.println("User exists and access token is alive!");
        }
    }
}
