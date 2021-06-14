package com.douglas.jointlyapp.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;

import androidx.navigation.NavDeepLinkBuilder;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.JointlyApplication;

import java.util.Random;

/**
 * Entity notifications
 */
public class Notifications {

    //TODO quizar quitar algo de aqui porque ya no haga falta

    /**
     * showNotificationAddInitiative
     * @param context
     * @param initiative
     */
    public static void showNotificationAddInitiative(Context context, long initiative) {
        Bundle bundle = new Bundle();
        bundle.putLong(Initiative.TAG, initiative);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.showInitiativeFragment)
                .setArguments(bundle)
                .createPendingIntent();

        Notification.Builder builder = new Notification.Builder(context, JointlyApplication.CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Nueva iniciativa")
                .setContentText("Douglas ha creado una nueva iniciativa")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(1000), builder.build());
    }

    /**
     * showNotificationNewMessageChat
     * @param context
     * @param initiative
     */
    public static void showNotificationNewMessageChat(Context context, long initiative) {
        Bundle bundle = new Bundle();
        bundle.putLong(Initiative.TAG, initiative);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.chatFragment)
                .setArguments(bundle)
                .createPendingIntent();

        Notification.Builder builder = new Notification.Builder(context, JointlyApplication.CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle("Nuevo mensaje")
                .setContentText("Nuevo mensaje en el chat de una iniciativa")
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(1000), builder.build());
    }
}