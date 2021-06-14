package com.douglas.jointlyapp.ui;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;

import com.douglas.jointlyapp.data.JointlyDatabase;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.broadcast.NotificationNewMessageChatBroadCast;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

/**
 * Entity main application
 */
public class JointlyApplication extends Application {

    public static final String FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String FORMAT_DD_MM_YYYY_HH_MM = "dd/MM/yyyy hh:mm";
    public static final String FORMAT_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd hh:mm";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";

    public static final String CHECK_CONNECTION_BROADCAST = "com.douglas.check_internet_connection";

    public static final int REQUEST_IMAGE_GALLERY = 101;
    public static final int REQUEST_PERMISSION_CODE = 100;
    public static final int REQUEST_PERMISSION_CAMERA_CODE = 1020;
    public static final String CHANNEL_ID = "1234";
    public static JobScheduler jobScheduler;

    private static User currentSignInUser;
    private static boolean connection = true;
    private static boolean isSyncronized;
    private static Context context;
    private static String token;

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

    public static Context getContext() {
        return context;
    }

    /**
     * create the notification channel
     */
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    public static boolean isIsSyncronized() {
        return JointlyPreferences.getInstance().getSync();
    }

    public static void setIsSyncronized(boolean isSyncronized) {
        JointlyPreferences.getInstance().putSync(isSyncronized);
    }

    public static void setCurrentSignInUser(User user) {
        JointlyPreferences.getInstance().putUser(user.getName(), user.getLocation(), user.getPhone(), user.getDescription());
        currentSignInUser = user;
    }

    public static User getCurrentSignInUser() {
        return currentSignInUser;
    }

    /**
     * save the token on Firebase database for notification
     * @param t
     */
    public static void saveToken(String t){
        token = t;
    }

    /**
     * get the token to reference the user for notification
     * @return String
     */
    public static String getToken() {
        return token;
    }
}
