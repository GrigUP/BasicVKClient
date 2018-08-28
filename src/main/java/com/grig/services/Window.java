package com.grig.services;

import com.grig.interfaces.*;
import javafx.stage.Stage;

public class Window {
    private String title;
    private String fxmlPath;
    private Stage stage;
    private Controller controller;
    private WindowHandler windowHandler;

    public Window(String title, String fxmlPath, Controller controller) {
        this.title = title;
        this.fxmlPath = fxmlPath;
        this.controller = controller;
    }

    public String getTitle() {
        return title;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public Stage getStage() {
        return stage;
    }

    public Controller getController() {
        return controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
