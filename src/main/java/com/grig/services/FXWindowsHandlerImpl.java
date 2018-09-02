package com.grig.services;

import com.grig.interfaces.WindowHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FXWindowsHandlerImpl implements WindowHandler {
    private Window currentWindow;
    private FXMLLoader fxmlLoader;
    private List<Window> windowsList = new ArrayList<>();

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
        currentWindow.getStage().show();
    }

    @Override
    public void show() {
        currentWindow.getStage().show();
    }

    @Override
    public void close() {
        currentWindow.getStage().close();
    }

    private Window getWindowByName(String windowName) {
        for (Window window:windowsList) {
            if (window.getTitle().equals(windowName)) return window;
        }

        return null;
    }
}
