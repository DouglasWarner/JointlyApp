package com.douglas.jointlyapp.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.Notifications;

//TODO quizas quitar esto
public class NotificationNewMessageChatBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notifications.showNotificationNewMessageChat(context, intent.getExtras().getLong(Initiative.TAG));
    }
}
