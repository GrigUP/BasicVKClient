package com.grig.main;

import com.grig.controller.MainController;
import com.grig.controller.MessageController;
import com.grig.interfaces.WindowHandler;
import com.grig.model.Model;
import com.grig.services.FXWindowsHandlerImpl;
import com.grig.services.Window;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        Model model = new Model();

        WindowHandler windowHandler = new FXWindowsHandlerImpl(primaryStage);
        windowHandler.addAllWindows(new Window("mainWindow","/MainWindow.fxml", new MainController(model)));
        windowHandler.addAllWindows(new Window("messageWindow", "/MessageWindow.fxml", new MessageController(model)));

        windowHandler.changeWindow("mainWindow");
        windowHandler.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
