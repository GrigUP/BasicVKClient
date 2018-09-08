package com.grig.json.get_history_response_json;

public class Item {
    int date;
    int from_id;
    int id;
    int out;
    int peer_id;
    String body;
    int conversation_message_id;
    boolean important;
    int random_id;
    Attachment[] attachments;
    boolean is_hidden;

    public int getDate() {
        return date;
    }

    public int getFrom_id() {
        return from_id;
    }

    public int getId() {
        return id;
    }

    public int getOut() {
        return out;
    }

    public int getPeer_id() {
        return peer_id;
    }

    public String getBody() {
        return body;
    }

    public int getConversation_message_id() {
        return conversation_message_id;
    }

    public boolean isImportant() {
        return important;
    }

    public int getRandom_id() {
        return random_id;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }

    public boolean isIs_hidden() {
        return is_hidden;
    }

    @Override
    public String toString() {
        return from_id + ": " + body;
    }
}
