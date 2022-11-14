package com.example.uberapp_tim13.items;

public class MessageItem {

    private String message;
    private User sender;
    private String time;

    public MessageItem(String message, User sender, String time) {
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
