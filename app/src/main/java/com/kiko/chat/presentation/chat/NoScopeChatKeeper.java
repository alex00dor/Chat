package com.kiko.chat.presentation.chat;

import com.kiko.chat.domain.model.Message;

import java.util.ArrayList;
import java.util.List;

public class NoScopeChatKeeper {

    String receiver = null;
    boolean isPause = false;
    List<Message> dataSet = new ArrayList<>();

    static NoScopeChatKeeper instance = null;

    private NoScopeChatKeeper() {
    }

    public static NoScopeChatKeeper getInstance(){
        if(instance == null){
            instance = new NoScopeChatKeeper();
        }
        return instance;
    }

    public void cleanReceiver(){
        this.receiver = null;
    }

    public void cleanDataSet(){
        this.dataSet = new ArrayList<>();
    }

    public List<Message> getDataSet() {
        return this.dataSet;
    }

    public void setDataSet(List<Message> dataSet) {
        this.dataSet = new ArrayList<>(dataSet);
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
