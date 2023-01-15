package com.example.uberapp_tim13.model;

public class Message {

    private String message;
    private int sender;
    private String time;

    public Message(String message, int sender, String time) {
        this.message = message;
        this.sender = sender;
        this.time = time.substring(11, 16);
    }

    public String getMessage() {
        return message;
    }

    public int getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }
}
