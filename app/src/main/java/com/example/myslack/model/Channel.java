package com.example.myslack.model;

import java.util.ArrayList;
import java.util.List;

public class Channel {

    private String lastMessage;
    private List<String> userIds;
    private List<User> users;

    public Channel() {
    }

    public Channel(String lastMessage, ArrayList<String> userIds, ArrayList<User> users) {
        this.lastMessage = lastMessage;
        this.userIds = userIds;
        this.users = users;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
