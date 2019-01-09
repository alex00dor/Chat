package com.kiko.chat.presentation.contactlist;

import com.kiko.chat.di.scope.PerFragment;
import com.kiko.chat.domain.interactor.ContactInteractor;
import com.kiko.chat.presentation.addcontact.AddContactModule;
import com.kiko.chat.presentation.contactlist.ui.ContactListAdapter;
import com.kiko.chat.presentation.contactlist.ui.ContactListFragment;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AddContactModule.class})
public abstract class ContactListModule {

    @PerFragment
    @Provides
    static ContactListPresenter provideLoginPresenter(
            ContactListPresenter.View view,
            ContactInteractor contactInteractor) {
        return new ContactListPresenterImpl(view, contactInteractor);
    }

    @PerFragment
    @Provides
    static ContactListPresenter.View provideContactListView(ContactListFragment fragment) {
        return fragment;
    }

    @PerFragment
    @Provides
    static ContactListAdapter provideContactListAdapter(ContactListFragment fragment) {
        return new ContactListAdapter(new ArrayList<>(), fragment);
    }

}
