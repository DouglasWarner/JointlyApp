package com.douglas.jointlyapp.ui.broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.Notifications;
import com.douglas.jointlyapp.ui.chat.ChatFragment;

public class NotificationNewMessageChatBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notifications.showNotificationNewMessageChat(context, intent.getExtras().getInt(Initiative.TAG));
    }
}
