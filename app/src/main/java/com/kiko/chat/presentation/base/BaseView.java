package com.kiko.chat.presentation.base;


public interface BaseView {
    void showProgress();
    void hideProgress();
    void showError(String message);
}
