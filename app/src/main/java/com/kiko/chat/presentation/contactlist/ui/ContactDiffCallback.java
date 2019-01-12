package com.kiko.chat.presentation.contactlist.ui;

import android.support.v7.util.DiffUtil;

import com.kiko.chat.domain.entity.Contact;

import java.util.List;

public class ContactDiffCallback extends DiffUtil.Callback {

    List<Contact> oldList;
    List<Contact> newList;

    public ContactDiffCallback(List<Contact> oldList, List<Contact> newList) {
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
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getEmail().equals(newList.get(newItemPosition).getEmail());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getLastMessage().equals(newList.get(newItemPosition).getLastMessage())
                && oldList.get(oldItemPosition).isOnline() == newList.get(newItemPosition).isOnline()
                && oldList.get(oldItemPosition).getNickName().equals(newList.get(newItemPosition).getNickName())
                && oldList.get(oldItemPosition).getPhotoUrl().equals(newList.get(newItemPosition).getPhotoUrl());
    }

}
