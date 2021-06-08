package com.douglas.jointlyapp.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.services.Apis;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class CheckConnectionService extends Service {

    CheckInternetAsyncTask checkInternetAsyncTask;

    @Override
    public void onCreate() {
        super.onCreate();
        checkInternetAsyncTask = new CheckInternetAsyncTask(JointlyApplication.getContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkInternetAsyncTask.start();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        checkInternetAsyncTask.interrupt();
    }

    private class CheckInternetAsyncTask extends Thread {

        private final Context context;

        public CheckInternetAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            while(true) {
                Intent checkBroadCast = new Intent(JointlyApplication.CHECK_CONNECTION_BROADCAST);
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = null;

                if (cm != null) {
                    activeNetwork = cm.getActiveNetworkInfo();
                } else {
                    checkBroadCast.putExtra("connection", false);
                }

                boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

                if (isConnected) {
                    Call<APIResponse<Object>> testCall = Apis.getInstance().getJointlyService().testConnection();
                    try {
                        Response<APIResponse<Object>> response = testCall.execute();
                        if (response.isSuccessful() &&
                                response.body() != null) {
                            checkBroadCast.putExtra("connection", true);
                        } else {
                            checkBroadCast.putExtra("connection", false);
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "Error checking internet connection");
                        checkBroadCast.putExtra("connection", false);
                    }
                } else {
                    Log.d("TAG", "No network available!");
                    checkBroadCast.putExtra("connection", false);
                }


                LocalBroadcastManager.getInstance(context).sendBroadcast(checkBroadCast);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}