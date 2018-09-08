package com.grig.services;

public class MessageManager {

    public static String sendMessage(int id, String message, Token token) {
        return RequestManager.sendRequest(getMessageUrl(id, message, token));
    }

    public static String getMessageList(int offset, int count, Token token) {
        return RequestManager.sendRequest(getMessageListUrl(offset, count, 1, token));
    }

    public static String getMessageHistory(int id, int offset, int count, Token token) {
        return RequestManager.sendRequest(getMessageHistoryByIdUrl(id, offset, count, token));
    }

    private static String getMessageHistoryByIdUrl(int id, int offset, int count, Token token) {
        return String.format("https://api.vk.com/method/messages.getHistory?user_id=%d&" +
                "offset=%d&" +
                "count=%d&" +
                "access_token=%s&" +
                "v=%s", id, offset, count, token.getAccess_token(), "5.8");
    }


    private static String getMessageUrl(int id, String message, Token token) {
        return String.format("https://api.vk.com/method/messages.send?user_id=%d&" +
                "message=%s&" +
                "access_token=%s&" +
                "v=%s", id, message, token.getAccess_token(), "5.8");
    }

    private static String getMessageListUrl(int offset, int count, int extended, Token token) {
        return String.format("https://api.vk.com/method/messages.getConversations?offset=%d&" +
                "count=%d&" +
                "extended=%d&" +
                "access_token=%s&" +
                "v=%s", offset, count, extended, token.getAccess_token(), "5.8");
    }
}
