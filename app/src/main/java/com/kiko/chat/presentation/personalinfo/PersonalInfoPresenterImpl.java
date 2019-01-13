package com.kiko.chat.presentation.personalinfo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.net.Uri;

import com.kiko.chat.domain.interactor.StorageInteractor;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class PersonalInfoPresenterImpl implements PersonalInfoPresenter, LifecycleObserver {

    private PersonalInfoPresenter.View view;
    private StorageInteractor storageInteractor;
    private UserAuthorizationInteractor userInteractor;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public PersonalInfoPresenterImpl(View view, StorageInteractor storageInteractor, UserAuthorizationInteractor userInteractor) {
        this.view = view;
        this.storageInteractor = storageInteractor;
        this.userInteractor = userInteractor;
    }

    @Override
    public void save(String nickname, String photoUrl) {
        disposables.add(userInteractor.updateUserInformation(nickname, photoUrl)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposables -> view.showProgress())
                .doAfterTerminate(() -> view.hideProgress())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.personalInfoSaved(),
                        throwable -> onError(throwable.getLocalizedMessage())));
    }

    @Override
    public void saveImageInStorage(Uri uri) {
        disposables.add(storageInteractor.loadImage(uri)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> view.showProgress())
                .doAfterTerminate(() -> view.hideProgress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> view.setPhotoUrl(s),
                        throwable -> view.showError(throwable.getLocalizedMessage())));
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
        disposables.clear();
        view = null;

    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }
}
