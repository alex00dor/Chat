package com.kiko.chat.presentation.chat.ui;

import android.support.v7.util.DiffUtil;

import com.kiko.chat.domain.entity.Message;

import java.util.List;

public class ChatDiffCallback extends DiffUtil.Callback {

    List<Message> oldList;
    List<Message> newList;

    public ChatDiffCallback(List<Message> oldList, List<Message> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return oldList.get(i).getuID().equals(newList.get(i1).getuID());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {

        return oldList.get(i).isSent() == newList.get(i1).isSent()
                && oldList.get(i).isRead() == newList.get(i1).isRead()
                && oldList.get(i).getMassage().equals(newList.get(i1).getMassage());
    }
}
