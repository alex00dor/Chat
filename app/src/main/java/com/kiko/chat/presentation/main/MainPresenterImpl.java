package com.kiko.chat.presentation.main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenterImpl implements MainPresenter, LifecycleObserver {

    private MainPresenter.View view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private UserAuthorizationInteractor sessionInteractor;

    public MainPresenterImpl(View view, UserAuthorizationInteractor sessionInteractor) {
        this.view = view;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        sessionInteractor.initConnection();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {

    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {

    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        disposables.clear();
        view = null;

    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }

    @Override
    public void logout() {
        disposables.add(sessionInteractor.logout()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> view.showProgress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            view.hideProgress();
                            view.navigateToLoginScreen();
                        },
                        throwable -> {
                            view.hideProgress();
                            onError(throwable.getLocalizedMessage());
                        }));
    }
}
