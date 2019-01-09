package com.kiko.chat.domain.interactor;

import com.kiko.chat.domain.repository.AuthorizationRepository;

import io.reactivex.Completable;

public class NewUserInteractor {
    AuthorizationRepository authorizationRepository;

    public NewUserInteractor(AuthorizationRepository authorizationRepository) {
        this.authorizationRepository = authorizationRepository;
    }

    public Completable newUser(String username, String password){
        return authorizationRepository.newUser(username, password);
    }
}
