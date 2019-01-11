package com.kiko.chat.presentation.login;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.kiko.chat.domain.interactor.NewUserInteractor;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenterImpl implements LoginPresenter, LifecycleObserver {

    private LoginPresenter.View mView;
    private UserAuthorizationInteractor sessionInteractor;
    private NewUserInteractor newUserInteractor;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public LoginPresenterImpl(View mView, UserAuthorizationInteractor sessionInteractor, NewUserInteractor newUserInteractor) {
        this.mView = mView;
        this.sessionInteractor = sessionInteractor;
        this.newUserInteractor = newUserInteractor;
    }

    @Override
    public void checkForAuthenticatedUser() {
        disposables.add(sessionInteractor.checkCurrentSession()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> mView.disableInputs())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mView.navigateToMainScreen(),
                        throwable -> mView.enableInputs()));
    }

    @Override
    public void validateLogin(String email, String password) {
        disposables.add(sessionInteractor.login(email, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mView.showProgress();
                    mView.disableInputs();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            mView.hideProgress();
                            mView.navigateToMainScreen();
                        },
                        throwable -> {
                            mView.hideProgress();
                            mView.enableInputs();
                            onError(throwable.getLocalizedMessage());
                        }));
    }

    @Override
    public void registerNewUser(final String email, final String password) {
        disposables.add(newUserInteractor.newUser(email, password)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mView.showProgress();
                    mView.disableInputs();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            mView.hideProgress();
                            validateLogin(email, password);
                        },
                        throwable -> {
                            mView.hideProgress();
                            mView.enableInputs();
                            onError(throwable.getLocalizedMessage());
                        }));
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {

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
        mView = null;
        disposables.clear();
    }

    @Override
    public void onError(String message) {
        mView.showError(message);
    }
}
