package com.grig.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grig.interfaces.Controller;
import com.grig.interfaces.WindowHandler;
import com.grig.json.*;
import com.grig.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;

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
    ListView<String> messageList;

    @FXML
    public void initialize() {
        updateMessageList();
    }

    public void sendButton(ActionEvent actionEvent) {
//        int id = Integer.parseInt(idTextField.getText());
//        String message = textArea.getText();
//        model.sendMessage(id, message);
    }

    public void updateMessageList() {
        Gson gson = new Gson();
        String JSONString = model.getConversations(0, 50);
        System.out.println(JSONString);
        JSONMessage jsonMessage = gson.fromJson(JSONString, JSONMessage.class);

        List<String> messages = new ArrayList<>();
        for (Items items:jsonMessage.getResponseJSON().getItems()) {
            if (items.getLast_message().getAttachments().length != 0 && items.getLast_message().getAttachments()[0].getType().equals("sticker")) {
                messages.add("[sticker]");
            } else if (items.getLast_message().getAttachments().length != 0 && items.getLast_message().getAttachments()[0].getType().equals("wall")) {
                messages.add("[wall]");
            } else messages.add(items.getLast_message().getText());
        }

        ObservableList<String> observableList = FXCollections.observableArrayList(messages);
        messageList.setItems(observableList);
        messageList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
}
