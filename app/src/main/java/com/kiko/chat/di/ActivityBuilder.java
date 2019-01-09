package com.kiko.chat.di;

import com.kiko.chat.di.scope.PerActivity;
import com.kiko.chat.presentation.login.LoginModule;
import com.kiko.chat.presentation.login.ui.LoginActivity;
import com.kiko.chat.presentation.main.MainModule;
import com.kiko.chat.presentation.main.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = {MainModule.class})
    abstract MainActivity bindMainActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = {LoginModule.class})
    abstract LoginActivity bindLoginActivity();
}
