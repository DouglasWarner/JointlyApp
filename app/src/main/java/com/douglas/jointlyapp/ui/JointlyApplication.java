package com.douglas.jointlyapp.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.model.UserReviewUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.ui.broadcast.NotificationNewMessageChatBroadCast;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class JointlyApplication extends Application {

    public static final String DATEFORMAT = "yyyy/MM/dd";
    public static final String DATEFORMAT2 = "dd/MM/yyyy";
    public static final String DATETIMEFORMAT = "dd/MM/yyyy hh:mm";
    public static final String CHECK_CONNECTION_BROADCAST = "com.douglas.check_internet_connection";

    public static final int REQUEST_IMAGE_GALLERY = 101;
    public static final int REQUEST_PERMISSION_CODE = 100;
    public static final String CHANNEL_ID = "1234";
    public static JobScheduler jobScheduler;

    private static boolean connection;
    private static Context context;

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


    public static boolean getConnection() {
        return connection;
    }

    public static void setConnection(boolean isConnectionAvailable) {
        connection = isConnectionAvailable;
    }
}
