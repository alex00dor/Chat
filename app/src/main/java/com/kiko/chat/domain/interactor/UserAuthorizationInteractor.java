package com.kiko.chat.domain.interactor;

import com.kiko.chat.domain.repository.AuthorizationRepository;

import io.reactivex.Completable;

public class UserAuthorizationInteractor {

    AuthorizationRepository repository;

    public UserAuthorizationInteractor(AuthorizationRepository repository) {
        this.repository = repository;
    }

    public Completable checkCurrentSession(){
        return repository.checkCurrentSession();
    }

    public Completable login(String username, String password){
        return repository.login(username, password);
    }

    public Completable sendNotificationKey(String key){
        return repository.sendNotificationKey(key);
    }

    public Completable updateUserInformation(String nickname, String photoUrl){
        return repository.updateUserInfo(nickname, photoUrl);
    }

    public Completable logout(){
        return repository.logout();
    }

    public void initConnection(){
        repository.initConnection();
    }
}
