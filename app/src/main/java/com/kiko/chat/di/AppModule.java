package com.kiko.chat.di;

import android.app.Application;
import android.content.Context;

import com.kiko.chat.datasource.network.FirebaseMessagingService;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class AppModule {

    @Binds
    abstract Context provideContext(Application application);

}
