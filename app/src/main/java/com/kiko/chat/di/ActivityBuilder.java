package com.kiko.chat.di;

import com.kiko.chat.di.scope.PerActivity;
import com.kiko.chat.presentation.login.di.LoginModule;
import com.kiko.chat.presentation.login.ui.LoginActivity;
import com.kiko.chat.presentation.main.di.MainModule;
import com.kiko.chat.presentation.main.ui.MainActivity;
import com.kiko.chat.presentation.personalinfo.di.PersonalInfoModule;
import com.kiko.chat.presentation.personalinfo.ui.PersonalInfoActivity;

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

    @PerActivity
    @ContributesAndroidInjector(modules = {PersonalInfoModule.class})
    abstract PersonalInfoActivity bindPersonalInfoActivity();
}
