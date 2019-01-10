package com.kiko.chat.presentation.login.di;

import com.kiko.chat.di.scope.PerActivity;
import com.kiko.chat.domain.interactor.NewUserInteractor;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;
import com.kiko.chat.presentation.login.LoginPresenter;
import com.kiko.chat.presentation.login.LoginPresenterImpl;
import com.kiko.chat.presentation.login.ui.LoginActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class LoginModule {

    @PerActivity
    @Provides
    static LoginPresenter provideLoginPresenter(LoginPresenter.View view, UserAuthorizationInteractor sessionInteractor, NewUserInteractor newUserInteractor){
        return new LoginPresenterImpl(view, sessionInteractor, newUserInteractor);
    }

    @Binds
    abstract LoginPresenter.View provideLoginView(LoginActivity activity);
}
