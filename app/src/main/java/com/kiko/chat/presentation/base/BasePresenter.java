package com.kiko.chat.presentation.base;

public interface BasePresenter {

    void resume();
    void pause();
    void stop();
    void destroy();


    void onError(String message);
}
