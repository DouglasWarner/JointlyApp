package com.douglas.jointlyapp.ui;

import android.app.Application;
import android.content.Context;

import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

public class JointlyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        JointlyPreferences.newInstance(this);

        context = getApplicationContext();
    }

    public static Context getContext()
    {
        return context;
    }
}
