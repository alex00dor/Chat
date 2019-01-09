package com.kiko.chat.di;

import android.content.Context;

import com.kiko.chat.presentation.chat.NoScopeChatKeeper;
import com.kiko.chat.presentation.util.NotificationHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
abstract class NoScopeModule {

    @Provides
    static NoScopeChatKeeper provideNoScopeChatKeeper(){
        return NoScopeChatKeeper.getInstance();
    }

    @Singleton
    @Provides
    static NotificationHelper provideNotificationHelper(Context context){
        return new NotificationHelper(context);
    }
}
