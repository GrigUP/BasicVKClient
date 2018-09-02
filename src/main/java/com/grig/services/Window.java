package com.grig.services;

import com.grig.interfaces.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Window {
    private Stage stage = new Stage();
    private FXMLLoader fxmlLoader;
    private Controller controller;

    public Window(String title, String fxmlPath, Controller controller) {
        fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        fxmlLoader.setController(controller);
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(title);
    }

    public Controller getController() {
        return fxmlLoader.getController();
    }

    public Stage getStage() {
        return stage;
    }

    public String getTitle() {
        return stage.getTitle();
    }
}
