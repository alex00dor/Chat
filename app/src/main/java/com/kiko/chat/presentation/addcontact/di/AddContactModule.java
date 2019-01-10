package com.kiko.chat.presentation.addcontact.di;

import com.kiko.chat.di.scope.PerFragment;
import com.kiko.chat.domain.interactor.ContactInteractor;
import com.kiko.chat.presentation.addcontact.AddContactPresenter;
import com.kiko.chat.presentation.addcontact.AddContactPresenterImpl;
import com.kiko.chat.presentation.addcontact.ui.AddContactDialogFragment;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class AddContactModule {

    @PerFragment
    @Provides
    static AddContactPresenter provideAddProvidePresenter(AddContactPresenter.View view, ContactInteractor interactor) {
        return new AddContactPresenterImpl(view, interactor);
    }

    @PerFragment
    @Provides
    static AddContactPresenter.View provideAddContactView(AddContactDialogFragment addContactDialogFragment) {
        return addContactDialogFragment;
    }

}
