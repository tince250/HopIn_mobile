package com.example.uberapp_tim13.model;

public class Message {

    private String message;
    private User sender;
    private String time;

    public Message(String message, User sender, String time) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public User getSender() {
        return sender;
    }

    public String getTime() {
        return time;
    }
}
