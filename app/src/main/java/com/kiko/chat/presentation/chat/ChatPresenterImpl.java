package com.kiko.chat.presentation.chat;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.net.Uri;

import com.kiko.chat.domain.interactor.ChatInteractor;
import com.kiko.chat.domain.interactor.StorageInteractor;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ChatPresenterImpl implements ChatPresenter, LifecycleObserver {

    private ChatPresenter.View view;
    private ChatInteractor chatInteractor;
    private StorageInteractor storageInteractor;
    private String receiver;
    private String sender;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private boolean initCache = false;
    private boolean syncReadMessages = false;

    public ChatPresenterImpl(
            ChatPresenter.View view,
            ChatInteractor chatInteractor,
            StorageInteractor storageInteractor,
            String receiver,
            String sender
    ) {
        this.view = view;
        this.chatInteractor = chatInteractor;
        this.storageInteractor = storageInteractor;
        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public void sendMessage(String msg) {
        disposables.add(chatInteractor.sendMessage(msg, receiver, sender, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, t -> onError(t.getLocalizedMessage())));
    }

    @Override
    public void loadImage(Uri uri) {
        disposables.add(storageInteractor.loadImage(uri)
                .flatMapCompletable(s -> chatInteractor.sendMessage(s, receiver, sender, true))
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> view.showProgress())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.hideProgress(),
                        throwable -> {
                            view.hideProgress();
                            view.showError(throwable.getLocalizedMessage());
                        }));
    }


    @Override
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume() {
        view.cancelNotification();
        if (initCache)
            subscribeOnMessage();
        else
            initCache();
    }

    private void initCache() {
        disposables.add(chatInteractor.getCachedMessages(receiver, sender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messages -> {
                    view.setMessagesDataSet(messages);
                    initCache = true;
                    subscribeOnMessage();
                }));
    }

    private void subscribeOnMessage() {
        disposables.add(chatInteractor.getChatObservable(receiver, sender)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> {
                            view.newMessage(message);
                            if (!message.isSendByCurrentUser() && !message.isRead() && !syncReadMessages) {
                                syncReadMessages = true;
                                disposables.add(chatInteractor
                                        .setReadForMessages(receiver, sender)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> syncReadMessages = false));
                            }
                        },
                        throwable -> onError(throwable.getLocalizedMessage())));
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
}
