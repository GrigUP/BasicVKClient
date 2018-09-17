package com.grig.controller;

import com.google.gson.Gson;
import com.grig.interfaces.Controller;
import com.grig.interfaces.WindowsManager;
import com.grig.json.for_conversation_response_json.JSONConversationMessage;
import com.grig.json.get_history_response_json.Item;
import com.grig.json.get_history_response_json.JSONHistory;
import com.grig.json.get_users_response_json.ResponseJSON;
import com.grig.model.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.util.*;

public class MessageController implements Controller {
    private WindowsManager windowsManager;
    private Model model;

    public MessageController(Model model) {
        this.model = model;
    }

    /**
     * Каждому контроллеру в соотвутсвие ставится экземпляр
     * WindowsManager - аналогия слою view по MVC.
     * @param windowsManager
     */
    @Override
    public void setWindowsManager(WindowsManager windowsManager) {
        this.windowsManager = windowsManager;
    }

    @FXML
    ListView<ResponseJSON> messageList;

    @FXML
    ListView<Item> messages;

    @FXML
    TextField textMessages;

    /**
     * Инициализация, выполняющаяся сразу после создания
     * экземпляра класса контроллера. Установка таймера по обновлению
     * листа диалогов и установка слушателя на загрузку истории сообщений по нажатию ЛКМ.
     */
    @FXML
    public void initialize() {
        updateMessageList();
//        // Таймер по обновлению листа диалогов
//        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
//
//            @Override
//            public void handle(ActionEvent event) {
//                updateMessageList();
//            }
//        }));
//        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
//        fiveSecondsWonder.play();

        // Наблюдатель ЛКМ для обновления истории сообщений для выбранного диалога
        messageList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateMessageHistory();
            }
        });
    }


    /**
     * Наблюдатель нажатия кнопки. По нажатию отправляет сообщения из поля ввода,
     * если оно не пустое..
     * @param actionEvent
     */
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
        } else {
            ScrollBar scrollbar = null;
            for (Node node : messages.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar) {
                    ScrollBar bar = (ScrollBar) node;
                    if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                        System.out.println(bar);
                    }
                }
            }
        }
    }


    /**
     * Метод обновляющий список диалогов для пользователя с user_id
     */
    private void updateMessageList() {
        Gson gson = new Gson();
        String jsonStrConversations = model.getConversations(0, 15);
        if (jsonStrConversations.contains("error")) {
            System.out.println("error");
            return;
        }

        JSONConversationMessage jsonWithConversations = gson.fromJson(jsonStrConversations, JSONConversationMessage.class);
        ArrayList<Integer> usersId = new ArrayList<>();

        // запись всех id пользователей из ответа на запрос в лист
        for (com.grig.json.for_conversation_response_json.Item items:jsonWithConversations.getResponseJSON().getItems()) {
            usersId.add(items.getLast_message().getPeer_id());
        }

        // получение информации по полученным id пользователей
        // в виде карты с ключем = id, значение = ResponseJSON (см. get_users_response_json)
        Map<Integer, ResponseJSON> profiles = model.getUsersInfoByIds(usersId);

        // запись в лист данных о пользователе
        List<ResponseJSON> messages = new ArrayList<>();
        for (int id:usersId) {
            messages.add(profiles.get(id));
        }

        // запись всех пользователей на ListView
        ObservableList<ResponseJSON> observableList = FXCollections.observableArrayList(messages);
        messageList.setItems(observableList);
        messageList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Метод определяющий историю сообщений между пользователем user_id и выбранным собеседником
     */
    private void updateMessageHistory() {

        // определяем выбранный диалог
        ResponseJSON user = messageList.getSelectionModel().getSelectedItem();
        int selectedUserId = user.getId();

        // для данного диалога запрашиваем с сервера историю
        Gson gson = new Gson();
        JSONHistory jsonHistory = gson.fromJson(model.getMessageHistory(selectedUserId, 0, 30), JSONHistory.class);

        // собираем историю в лист
        ArrayList<Item> itemsList = new ArrayList<>();
        for (Item item:jsonHistory.getResponse().getItems()) {
            itemsList.add(item);
        }

        // выводим лист в ListView
        Collections.reverse(itemsList);
        ObservableList<Item> observableList = FXCollections.observableArrayList(itemsList);
        messages.setItems(observableList);

        messages.scrollTo(observableList.size()-1);
    }
}
