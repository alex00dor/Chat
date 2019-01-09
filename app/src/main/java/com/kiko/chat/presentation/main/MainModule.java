package com.kiko.chat.presentation.main;

import com.kiko.chat.di.scope.PerActivity;
import com.kiko.chat.di.scope.PerFragment;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;
import com.kiko.chat.presentation.addcontact.AddContactModule;
import com.kiko.chat.presentation.addcontact.ui.AddContactDialogFragment;
import com.kiko.chat.presentation.chat.ChatModule;
import com.kiko.chat.presentation.chat.ui.ChatFragment;
import com.kiko.chat.presentation.contactlist.ContactListModule;
import com.kiko.chat.presentation.contactlist.ui.ContactListFragment;
import com.kiko.chat.presentation.main.ui.MainActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @PerActivity
    @Provides
    static MainPresenter provideMainPresenter(MainPresenter.View view, UserAuthorizationInteractor sessionInteractor){
        return new MainPresenterImpl(view, sessionInteractor);
    }

    @PerFragment
    @ContributesAndroidInjector(modules = {ContactListModule.class})
    abstract ContactListFragment bindContactListFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = {AddContactModule.class})
    abstract AddContactDialogFragment bindAddContactDialogFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = {ChatModule.class})
    abstract ChatFragment bindChatFragment();

    @Binds
    abstract MainPresenter.View provideMainView(MainActivity activity);
}
