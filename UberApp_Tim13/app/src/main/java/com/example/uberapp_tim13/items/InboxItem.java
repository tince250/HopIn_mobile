package com.example.uberapp_tim13.items;

public class InboxItem {
    String name;
    String displayMessage;
    String dateTime;
    int image;

    public InboxItem(String name, String displayMessage, String dateTime, int image) {
        this.name = name;
        this.displayMessage = displayMessage;
        this.dateTime = dateTime;
        this.image = image;
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
}
