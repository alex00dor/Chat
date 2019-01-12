package com.kiko.chat.presentation.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;

import com.kiko.chat.R;
import com.kiko.chat.presentation.main.ui.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String MESSAGE_CHANNEL = "Message Channel";
    public static final int MESSAGE_NOTIFY_ID = 1;
    public static final int GROUP_MESSAGE_NOTIFY_ID = 0;

    private int lastGroupID = -1;
    private Map<String, Integer> messageGroupKeeper = new HashMap<>();

    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(MESSAGE_CHANNEL,
                    getResources().getString(R.string.default_notification_channel_id),
                    NotificationManager.IMPORTANCE_DEFAULT);
            getManager().createNotificationChannel(channel);
        }
    }

    public int getGroupID(String key){
        if(!messageGroupKeeper.containsKey(key)){
            messageGroupKeeper.put(key, lastGroupID);
            lastGroupID -= 1;
        }
        return messageGroupKeeper.get(key);
    }

    public NotificationCompat.Builder getMessageNotification(String sender, String msg) {
        return new NotificationCompat.Builder(getApplicationContext(), MESSAGE_CHANNEL)
                .setContentTitle(sender)
                .setContentText(msg)
                .setSound(null)
                .setContentIntent(getIntentForChat(sender))
                .setSmallIcon(R.drawable.ic_message)
                .setGroup(sender)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getGroupNotification(String group, String channel, @DrawableRes int icon, PendingIntent groupIntent){
        NotificationCompat.Builder groupBuilder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentIntent(groupIntent)
                .setGroupSummary(true)
                .setGroup(group);

        return groupBuilder;
    }

    public PendingIntent getIntentForChat(String receiver){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TO_CHAT, receiver);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, getGroupID(receiver), intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }

    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public void notify(String tag, int id, NotificationCompat.Builder notification) {
        getManager().notify(tag, id, notification.build());
    }

    public void cancel(String tag, int id){
        getManager().cancel(tag, id);
    }

    public void cancel(int id){
        getManager().cancel(id);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
