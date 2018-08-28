package com.grig.controller;

import com.grig.interfaces.Controller;
import com.grig.interfaces.WindowHandler;
import com.grig.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MessageController implements Controller {
    private WindowHandler windowHandler;
    private Model model;

    public MessageController(Model model) {
        this.model = model;
    }

    @Override
    public void setWindowHandler(WindowHandler windowHandler) {
        this.windowHandler = windowHandler;
    }

    @FXML
    TextArea textArea;

    @FXML
    TextField idTextField;

    public void sendButton(ActionEvent actionEvent) {
        int id = Integer.parseInt(idTextField.getText());
        String message = textArea.getText();
        model.sendMessage(id, message);
    }
}
