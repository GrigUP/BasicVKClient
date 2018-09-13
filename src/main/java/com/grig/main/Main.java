package com.grig.main;

import com.grig.controller.MessageController;
import com.grig.database.DataBaseFactory;
import com.grig.database.dao.AuthInfoDao;
import com.grig.database.impl.AuthInfoDaoImpl;
import com.grig.interfaces.WindowsManager;
import com.grig.model.Model;
import com.grig.services.managers.AccountDataBaseManager;
import com.grig.services.managers.WindowsManagerImpl;
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

        AuthInfoDao authInfoDao = new AuthInfoDaoImpl(new DataBaseFactory(dbDriver, dbURL, dbUserName, dbPassword));
        AccountDataBaseManager accountDataBaseManager = new AccountDataBaseManager(authInfoDao);

        Model model = new Model(accountDataBaseManager);
//
        WindowsManager windowsManager = new WindowsManagerImpl();
        windowsManager.addAllWindows(new Window("messageWindow", "/MessageWindow.fxml", new MessageController(model)));


        windowsManager.changeWindow("messageWindow");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
