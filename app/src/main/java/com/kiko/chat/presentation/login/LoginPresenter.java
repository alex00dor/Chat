package com.kiko.chat.presentation.login;

import com.kiko.chat.presentation.base.BaseView;
import com.kiko.chat.presentation.base.BasePresenter;

public interface LoginPresenter extends BasePresenter {
    interface View extends BaseView{
        void enableInputs();
        void disableInputs();

        void navigateToMainScreen();
    }

    void checkForAuthenticatedUser();
    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);
}
