package com.example.uberapp_tim13.model;

public class Inbox {
    String name;
    String displayMessage;
    String dateTime;
    int image;
    String type;

    public Inbox(String name, String displayMessage, String dateTime, int image, String type) {
        this.name = name;
        this.displayMessage = displayMessage;
        this.dateTime = dateTime;
        this.image = image;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
