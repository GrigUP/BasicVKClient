package com.grig.main;

import com.grig.controller.MessageController;
import com.grig.database.DataBaseFactory;
import com.grig.database.dao.AuthInfoDao;
import com.grig.database.impl.AuthInfoDaoImpl;
import com.grig.database.model.AuthInfo;
import com.grig.interfaces.WindowHandler;
import com.grig.model.Model;
import com.grig.services.FXWindowsHandlerImpl;
import com.grig.services.Window;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        String dbDriver = "com.mysql.cj.jdbc.Driver";
        String dbURL = "jdbc:mysql://localhost:3306/vk_client_db";
        String dbUserName = "root";
        String dbPassword = "32553255";
        DataBaseFactory dataBaseFactory = new DataBaseFactory(dbDriver, dbURL, dbUserName, dbPassword);
        AuthInfoDao authInfoDao = new AuthInfoDaoImpl(dataBaseFactory);

        AuthInfo authInfo = new AuthInfo(1, "lolkek", 10, 1);

        authInfoDao.delete(authInfo);
//        Model model = new Model();
//
//        WindowHandler windowHandler = new FXWindowsHandlerImpl();
//        windowHandler.addAllWindows(new Window("messageWindow", "/MessageWindow.fxml", new MessageController(model)));
//
//
//        windowHandler.changeWindow("messageWindow");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
