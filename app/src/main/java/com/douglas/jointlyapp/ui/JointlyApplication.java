package com.douglas.jointlyapp.ui;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.ui.broadcast.NotificationNewMessageChatBroadCast;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.service.BackgroundJobService;

public class JointlyApplication extends Application {

    public static final int REQUEST_IMAGE_GALLERY = 101;
    public static final int REQUEST_PERMISSION_CODE = 100;
    public static final String CHANNEL_ID = "1234";
    private static Context context;
    public static JobScheduler jobScheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationNewMessageChatBroadCast broadCast = new NotificationNewMessageChatBroadCast();
        IntentFilter intentFilter = new IntentFilter("com.douglas.notification_new_message_chat");
        registerReceiver(broadCast, intentFilter);

        JointlyDatabase.create(this);
        JointlyPreferences.newInstance(this);
        createNotificationChannel();
        context = getApplicationContext();
    }

    public static Context getContext()
    {
        return context;
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Urgent", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            channel.setShowBadge(true);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
