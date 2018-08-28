package com.grig.services;

import com.grig.interfaces.WindowHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FXWindowsHandlerImpl implements WindowHandler {
    private Stage stage;
    private Window currentWindow;
    private FXMLLoader fxmlLoader;
    private List<Window> windowsList = new ArrayList<>();

    public FXWindowsHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void addAllWindows(Window... windows) {
        for (Window window:windows) {
            window.getController().setWindowHandler(this);
            windowsList.add(window);
        }
    }

    @Override
    public void changeWindow(String windowName) {
        Window newWindow = getWindowByName(windowName);
        currentWindow = newWindow;
        fxmlLoader = new FXMLLoader(getClass().getResource(currentWindow.getFxmlPath()));
        fxmlLoader.setController(currentWindow.getController());
        try {
            stage.setScene(new Scene((Parent)fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(windowName);
    }

    @Override
    public void show() {
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }

    private Window getWindowByName(String windowName) {
        for (Window window:windowsList) {
            if (window.getTitle().equals(windowName)) return window;
        }

        return null;
    }
}
