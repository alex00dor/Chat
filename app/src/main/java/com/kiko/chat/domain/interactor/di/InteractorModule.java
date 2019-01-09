package com.kiko.chat.domain.interactor.di;

import com.kiko.chat.domain.interactor.ChatInteractor;
import com.kiko.chat.domain.interactor.ContactInteractor;
import com.kiko.chat.domain.interactor.NewUserInteractor;
import com.kiko.chat.domain.interactor.StorageInteractor;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;
import com.kiko.chat.domain.repository.AuthorizationRepository;
import com.kiko.chat.domain.repository.ChatRepository;
import com.kiko.chat.domain.repository.ContactRepository;
import com.kiko.chat.domain.repository.StorageRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class InteractorModule {

    @Provides
    @Singleton
    static UserAuthorizationInteractor provideCheckAuthenticatedUserInteractor(AuthorizationRepository repository){
        return new UserAuthorizationInteractor(repository);
    }

    @Provides
    @Singleton
    static NewUserInteractor provideNewUserInteractor(AuthorizationRepository repository){
        return new NewUserInteractor(repository);
    }

    @Provides
    @Singleton
    static ContactInteractor provideContactRemoteInteractor(ContactRepository repository){
        return new ContactInteractor(repository);
    }

    @Provides
    @Singleton
    static ChatInteractor provideChatRemoteInteractor(ChatRepository repository){
        return new ChatInteractor(repository);
    }

    @Provides
    @Singleton
    static StorageInteractor provideStorageInteractor(StorageRepository repository){
        return new StorageInteractor(repository);
    }

}
