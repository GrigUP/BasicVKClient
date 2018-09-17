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

    /**
     * Смена текущего активного окна -> stage по исмени (title).
     * @param windowName
     */
    @Override
    public void changeWindow(String windowName) {
        Window newWindow = getWindowByName(windowName);
        currentWindow = newWindow;
        currentWindow.getStage().show();
    }

    /**
     * Метод по поиску в листе окна с текущим именем (title)
     * @param windowName
     * @return
     * Возвращает окно с title == windowName, иначе null.
     */
    private Window getWindowByName(String windowName) {
        for (Window window:windowsList) {
            if (window.getTitle().equals(windowName)) return window;
        }

        return null;
    }
}
