package com.kiko.chat.presentation.chat;

import android.net.Uri;

import com.kiko.chat.domain.model.Message;
import com.kiko.chat.presentation.base.BasePresenter;
import com.kiko.chat.presentation.base.BaseView;

import java.util.List;

public interface ChatPresenter extends BasePresenter {
    interface View extends BaseView{
        void newMessage(Message message);
        void setMessagesDataSet(List<Message> messages);
        void cancelNotification();
    }

    void sendMessage(String msg);
    void loadImage(Uri uri);
}
