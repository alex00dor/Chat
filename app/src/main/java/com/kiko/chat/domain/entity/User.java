package com.kiko.chat.domain.entity;

import java.util.Map;

public class User {

    String email;
    boolean online;
    Map<String, Boolean> contactList;
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User() {
    }

    public User(String email, boolean online, Map<String, Boolean> contactList) {
        this.email = email;
        this.online = online;
        this.contactList = contactList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Map<String, Boolean> getContactList() {
        return contactList;
    }

    public void setContactList(Map<String, Boolean> contactList) {
        this.contactList = contactList;
    }
}
