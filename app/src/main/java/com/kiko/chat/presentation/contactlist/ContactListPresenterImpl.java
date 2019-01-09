package com.kiko.chat.presentation.contactlist;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.kiko.chat.domain.event.ContactEvent;
import com.kiko.chat.domain.interactor.ContactInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ContactListPresenterImpl implements ContactListPresenter, LifecycleObserver {

    private ContactListPresenter.View view;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private ContactInteractor contactInteractor;
    private boolean initCache = false;


    public ContactListPresenterImpl(View view, ContactInteractor contactInteractor) {
        this.view = view;
        this.contactInteractor = contactInteractor;
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        if(initCache)
            subscribeOnContacts();
        else
            initCache();
    }

    void initCache(){
        disposables.add(contactInteractor.getCachedContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    view.setContactDataSet(contacts);
                    initCache = true;
                    subscribeOnContacts();
                }));
    }

    private void subscribeOnContacts(){
        disposables.add(contactInteractor.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactEvent -> {
                    switch (contactEvent.getType()) {
                        case ContactEvent.ACTION_CONTACT_ADD:
                            view.onContactAdded(contactEvent.getContact());
                            break;
                        case ContactEvent.ACTION_CONTACT_CHANGED:
                            view.onContactChanged(contactEvent.getContact());
                            break;
                        case ContactEvent.ACTION_CONTACT_DELETED:
                            view.onContactRemoved(contactEvent.getContact());
                            break;
                        default:
                            break;
                    }
                }, throwable -> onError(throwable.getLocalizedMessage())));
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void pause() {
        disposables.clear();
    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stop() {

    }

    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy() {
        view = null;

    }

    @Override
    public void onError(String message) {
        view.showError(message);
    }


    @Override
    public void removeContact(String email) {
        disposables.add(contactInteractor.removeContact(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(() -> {},
                throwable -> onError(throwable.getLocalizedMessage())));
    }
}
