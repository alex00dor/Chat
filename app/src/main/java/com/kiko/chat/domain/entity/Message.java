package com.kiko.chat.domain.entity;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String uID;

    private String massage;
    private String sender;
    private String receiver;
    private boolean read;
    private boolean image = false;
    private boolean sent;
    private long timestamp;

    private boolean isSendByCurrentUser;

    public Message() {
    }

    public Message(String massage, String sender, long timestamp) {
        this.massage = massage;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        if(massage != null)
            this.massage = massage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSendByCurrentUser() {
        return isSendByCurrentUser;
    }

    public void setSendByCurrentUser(boolean sendByCurrentUser) {
        isSendByCurrentUser = sendByCurrentUser;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("message", getMassage());
        map.put("sender", getSender());
        map.put("receiver", getReceiver());
        map.put("read", isRead());
        map.put("image", isImage());
        map.put("timestamp", getTimestamp());
        return map;
    }
}
