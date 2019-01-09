package com.kiko.chat.datasource.network;

import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.kiko.chat.R;
import com.kiko.chat.domain.interactor.UserAuthorizationInteractor;
import com.kiko.chat.presentation.chat.NoScopeChatKeeper;
import com.kiko.chat.presentation.util.NotificationHelper;

import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Inject
    UserAuthorizationInteractor interactor;
    @Inject
    NoScopeChatKeeper keeper;
    @Inject
    NotificationHelper notificationHelper;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        if (data.containsKey("sender") && data.containsKey("message")) {
            if (!Objects.equals(data.get("sender"), keeper.getReceiver()) || keeper.isPause()) {
                sendNotification(data.get("sender"), data.get("message"), remoteMessage.getMessageId());
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        disposables.add(interactor.sendNotificationKey(s)
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {}, throwable -> {}));
    }

    private void sendNotification(String sender, String message, String id) {

        NotificationCompat.Builder notification =
                notificationHelper.getMessageNotification(sender, message);

        NotificationCompat.Builder group =
                notificationHelper.getGroupNotification(sender, NotificationHelper.MESSAGE_CHANNEL,
                        R.drawable.ic_message, notificationHelper.getIntentForChat(sender));


        notificationHelper.notify(id, NotificationHelper.MESSAGE_NOTIFY_ID, notification);
        notificationHelper.notify(sender, notificationHelper.getGroupID(sender), group);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
