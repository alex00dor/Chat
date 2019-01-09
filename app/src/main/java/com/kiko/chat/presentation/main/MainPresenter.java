package com.kiko.chat.presentation.main;

import com.kiko.chat.presentation.base.BaseView;
import com.kiko.chat.presentation.base.BasePresenter;

public interface MainPresenter extends BasePresenter {
    interface View extends BaseView {
        void navigateToLoginScreen();
    }

    void logout();
}
