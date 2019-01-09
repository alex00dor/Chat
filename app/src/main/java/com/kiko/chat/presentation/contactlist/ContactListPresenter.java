package com.kiko.chat.presentation.contactlist;

import com.kiko.chat.domain.model.Contact;
import com.kiko.chat.presentation.base.BasePresenter;
import com.kiko.chat.presentation.base.BaseView;

import java.util.List;

public interface ContactListPresenter extends BasePresenter {
    interface View extends BaseView{
        void setContactDataSet(List<Contact> contactList);
        void onContactAdded(Contact contact);
        void onContactChanged(Contact contact);
        void onContactRemoved(Contact contact);
    }

    void removeContact(String email);
}
