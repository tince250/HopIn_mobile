package com.example.uberapp_tim13.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class InboxReturnedDTO implements Serializable {
    private int id;
    private UserReturnedDTO firstUser;
    private UserReturnedDTO secondUser;
    List<MessageReturnedDTO> messages = new ArrayList<MessageReturnedDTO>();
    private String lastMessage;
    private String type;

    public InboxReturnedDTO(int id, UserReturnedDTO firstUser, UserReturnedDTO secondUser, List<MessageReturnedDTO> messages) {
        this.id = id;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MessageReturnedDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageReturnedDTO> messages) {
        this.messages = messages;
    }

    public UserReturnedDTO getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(UserReturnedDTO firstUser) {
        this.firstUser = firstUser;
    }

    public UserReturnedDTO getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(UserReturnedDTO secondUser) {
        this.secondUser = secondUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
