package com.kiko.chat.domain.interactor;

import com.kiko.chat.domain.model.Message;
import com.kiko.chat.domain.repository.ChatRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ChatInteractor {
    ChatRepository repository;

    public ChatInteractor(ChatRepository repository) {
        this.repository = repository;
    }

    public Completable sendMessage(String msg, String receiver, String sender, boolean isImage){
        return repository.sendMessage(msg, receiver, sender, isImage);
    }

    public Completable setReadForMessages(String receiver, String sender){
        return repository.setReadMessages(receiver, sender);
    }

    public Flowable<Message> getChatObservable(String receiver, String sender){
        return repository.subscribeOnChat(receiver, sender);
    }

    public Single<List<Message>> getCachedMessages(String receiver, String sender){
        return repository.getCachedMessages(receiver, sender);
    }
}
