package com.kiko.chat.domain.repository;

import com.kiko.chat.domain.entity.Message;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ChatRepository {
    Completable sendMessage(String msg, String receiver, String sender, boolean isImage);
    Completable setReadMessages(String receiver, String sender);
    Flowable<Message> subscribeOnChat(String receiver, String sender);
    Single<List<Message>> getCachedMessages(String receiver, String sender);
}
