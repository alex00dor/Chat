package com.kiko.chat.domain.repository;

import io.reactivex.Completable;

public interface AuthorizationRepository {
    Completable login(String username, String password);
    Completable newUser(String username, String password);
    Completable checkCurrentSession();
    Completable logout();
    Completable sendNotificationKey(String key);
    Completable updateUserInfo(String nickname, String photoUrl);
    void initConnection();
}
