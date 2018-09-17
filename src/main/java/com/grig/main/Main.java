package com.grig.main;

import com.grig.controller.MessageController;
import com.grig.database.DBType;
import com.grig.database.JDBC.dao.AuthInfoDao;
import com.grig.database.JDBC.dao.impl.AuthInfoDaoImpl;
import com.grig.database.factory.impl.DataBaseFactoryImpl;
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

        AuthInfoDao authInfoDao = new AuthInfoDaoImpl(new DataBaseFactoryImpl(DBType.MY_SQL));
        AccountDataBaseManager accountDataBaseManager = new AccountDataBaseManager(authInfoDao);


        Model model = new Model(accountDataBaseManager);

        WindowsManager windowsManager = new WindowsManagerImpl();
        windowsManager.addAllWindows(new Window("Messages", "/MessageWindow.fxml", new MessageController(model)));


        windowsManager.changeWindow("Messages");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
