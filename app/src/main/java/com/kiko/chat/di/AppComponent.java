package com.kiko.chat.di;

import android.app.Application;

import com.kiko.chat.ChatApplication;
import com.kiko.chat.domain.interactor.di.InteractorModule;
import com.kiko.chat.datasource.network.di.NetworkModule;
import com.kiko.chat.libs.LibsModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class,
        NoScopeModule.class,
        LibsModule.class,
        NetworkModule.class,
        InteractorModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(ChatApplication app);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}