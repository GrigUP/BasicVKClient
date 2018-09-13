package com.grig.controller;

import com.google.gson.Gson;
import com.grig.interfaces.Controller;
import com.grig.interfaces.WindowHandler;
import com.grig.json.for_conversation_response_json.Items;
import com.grig.json.for_conversation_response_json.JSONMessage;
import com.grig.json.get_history_response_json.Item;
import com.grig.json.get_history_response_json.JSONHistory;
import com.grig.json.get_users_response_json.ResponseJSON;
import com.grig.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    ListView<ResponseJSON> messageList;

    @FXML
    ListView<Item> messages;

    @FXML
    TextField textMessages;

    @FXML
    Button button;

    @FXML
    public void initialize() {
        updateMessageList();
        messageList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateMessageHistory();
            }
        });
    }

    public void sendButton(ActionEvent actionEvent) {
        if (!textMessages.getText().isEmpty()) {
            ResponseJSON responseJSON = messageList.getSelectionModel().getSelectedItem();

            int id = responseJSON.getId();
            String message = textMessages.getText();
            message = message.replaceAll(" ", "%20");

            model.sendMessage(id, message);

            textMessages.setText("");
            updateMessageList();
            messageList.getSelectionModel().select(responseJSON);
            updateMessageHistory();
        }
    }

    private void updateMessageList() {
        Gson gson = new Gson();
        JSONMessage jsonMessage = gson.fromJson(model.getConversations(0, 50), JSONMessage.class);

        ArrayList<Integer> usersId = new ArrayList<>();

        for (Items items:jsonMessage.getResponseJSON().getItems()) {
            usersId.add(items.getLast_message().getPeer_id());
        }

        Map<Integer, ResponseJSON> profiles = model.getUsersInfoById(usersId);

        List<ResponseJSON> messages = new ArrayList<>();
        for (int id:usersId) {
            messages.add(profiles.get(id));
        }

        ObservableList<ResponseJSON> observableList = FXCollections.observableArrayList(messages);
        messageList.setItems(observableList);
        messageList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void updateMessageHistory() {
        ResponseJSON user = messageList.getSelectionModel().getSelectedItem();
        int selectedUserId = user.getId();
        Gson gson = new Gson();
        JSONHistory jsonHistory = gson.fromJson(model.getMessageHistory(selectedUserId, 0, 15), JSONHistory.class);

        ArrayList<Item> itemsList = new ArrayList<>();
        for (Item item:jsonHistory.getResponse().getItems()) {
            itemsList.add(item);
        }

        Collections.reverse(itemsList);
        ObservableList<Item> observableList = FXCollections.observableArrayList(itemsList);
        messages.setItems(observableList);

        messages.scrollTo(observableList.size()-1);
    }
}
