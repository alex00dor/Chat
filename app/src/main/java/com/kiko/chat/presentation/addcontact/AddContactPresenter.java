package com.kiko.chat.presentation.addcontact;

import com.kiko.chat.presentation.base.BasePresenter;
import com.kiko.chat.presentation.base.BaseView;

public interface AddContactPresenter extends BasePresenter {
    interface View extends BaseView {
        void showInput();
        void hideInput();
        void contactAdded();
    }

    void addContact(String email);
}
