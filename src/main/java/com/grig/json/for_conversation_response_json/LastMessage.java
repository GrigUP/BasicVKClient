package com.grig.json.for_conversation_response_json;

public class LastMessage {
    long date;
    int from_id;
    int id;
    int out;
    int peer_id;
    String text;
    int conversation_message_id;
    LastMessage[] fwd_messages;
    boolean important;
    int random_id;
    Attachment[] attachments;
    boolean is_hidden;

    public long getDate() {
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

    public String getText() {
        return text;
    }

    public int getConversation_message_id() {
        return conversation_message_id;
    }

    public LastMessage[] getFwd_messages() {
        return fwd_messages;
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
        return "id=" + peer_id + ", '" + text + '\'';
    }
}
