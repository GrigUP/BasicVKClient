package com.grig.services.managers;

import com.grig.interfaces.WindowsManager;
import com.grig.services.Window;
import javafx.fxml.FXMLLoader;

import java.util.ArrayList;
import java.util.List;

public class WindowsManagerImpl implements WindowsManager {
    private Window currentWindow;
    private List<Window> windowsList = new ArrayList<>();

    @Override
    public void addAllWindows(Window... windows) {
        for (Window window:windows) {
            window.getController().setWindowsManager(this);
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
