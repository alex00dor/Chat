package com.kiko.chat.presentation.addcontact;

import com.kiko.chat.domain.interactor.ContactInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddContactPresenterImpl implements AddContactPresenter {

    private AddContactPresenter.View view;
    private ContactInteractor interactor;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public AddContactPresenterImpl(View view, ContactInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void addContact(String email) {
        disposables.add(interactor.addContact(email)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    view.hideInput();
                    view.showProgress();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.contactAdded(),
                        throwable -> {
                            view.hideProgress();
                            view.showInput();
                            onError(throwable.getLocalizedMessage());
                        }));
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        view = null;
        disposables.clear();
    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }
}
