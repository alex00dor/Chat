package com.kiko.chat.presentation.personalinfo.di;

import com.kiko.chat.di.scope.PerActivity;
import com.kiko.chat.domain.interactor.StorageInteractor;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;
import com.kiko.chat.presentation.personalinfo.PersonalInfoPresenter;
import com.kiko.chat.presentation.personalinfo.PersonalInfoPresenterImpl;
import com.kiko.chat.presentation.personalinfo.ui.PersonalInfoActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class PersonalInfoModule {

    @PerActivity
    @Provides
    static PersonalInfoPresenter provideMainPresenter(
            PersonalInfoPresenter.View view,
            StorageInteractor storageInteractor,
            UserAuthorizationInteractor userInteractor){
        return new PersonalInfoPresenterImpl(view, storageInteractor, userInteractor);
    }


    @Binds
    abstract PersonalInfoPresenter.View provideMainView(PersonalInfoActivity activity);

}
