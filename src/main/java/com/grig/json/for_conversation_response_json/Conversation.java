package com.grig.json.for_conversation_response_json;

public class Conversation {
    Peer peer;
    int in_read;
    int out_read;
    int last_message_id;
    CanWrite can_write;

    public Peer getPeer() {
        return peer;
    }

    public int getIn_read() {
        return in_read;
    }

    public int getOut_read() {
        return out_read;
    }

    public int getLast_message_id() {
        return last_message_id;
    }

    public CanWrite getCan_write() {
        return can_write;
    }

    @Override
    public String toString() {
        return "from " +
                "peer=" + peer +
                ", last_message_id=" + last_message_id;
    }
}
