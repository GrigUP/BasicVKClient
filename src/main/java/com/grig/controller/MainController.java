package com.grig.controller;

import com.grig.interfaces.WindowHandler;
import com.grig.model.Model;
import com.grig.interfaces.Controller;
import com.grig.services.Token;
import com.grig.services.TokenSaverAndChecker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController implements Controller {
    private WindowHandler windowHandler;
    private Model model;

    public MainController(Model model) {
        this.model = model;
    }

    public void connectButton(ActionEvent actionEvent) {
        String request = null;
        try {
            request = TokenSaverAndChecker.getTokenFromFile(model.getTokenFilePath());
        } catch (IOException e) {
            System.out.println("error");
        }

        if (request.contains("access_token=") && request.contains("user_id=") && request.contains("expires_id=")) {
            String[] keys = model.getSecureDataFromResponce(request);
            model.setToken(new Token(keys[0], Integer.parseInt(keys[1]), Integer.parseInt(keys[2])));

            printDialogWindow("Key: " + model.getToken().getAccess_token());

            windowHandler.changeWindow("messageWindow");
            windowHandler.show();
        } else {
            final WebView webView = new WebView();
            final WebEngine webEngine = webView.getEngine();

            final Stage tempStage = new Stage();
            tempStage.setScene(new Scene(webView));
            webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                    if (newValue == Worker.State.SUCCEEDED && webView.getEngine().getDocument().getDocumentURI().contains("#access_token=")) {
                        String url = webView.getEngine().getDocument().getDocumentURI();
                        String[] keys = model.getSecureDataFromResponce(url);
                        Token token = new Token(keys[0], Integer.parseInt(keys[1]), Integer.parseInt(keys[2]));
                        model.setToken(token);
                        tempStage.close();

                        try {
                            TokenSaverAndChecker.writeTokenToFile(model.getTokenFilePath(), token);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        printDialogWindow("Key: " + model.getToken().getAccess_token());

                        windowHandler.changeWindow("messageWindow");
                        windowHandler.show();
                    }
                }
            });

            webEngine.load(model.getAccessTokenUrl());
            tempStage.show();
        }
    }

    @Override
    public void setWindowHandler(WindowHandler windowHandler) {
        this.windowHandler = windowHandler;
    }

    private void printDialogWindow(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
