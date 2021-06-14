package com.douglas.jointlyapp.ui.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.navigation.NavDeepLinkBuilder;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotification extends FirebaseMessagingService {

    public static final String ATOPICO = "ATOPICO";
    public static final String TOPICO = "TOPICO";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        JointlyApplication.saveToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String messageFrom = remoteMessage.getFrom();

        Log.e("TAG", messageFrom);
        if(remoteMessage.getData().size() > 0 && JointlyPreferences.getInstance().getNotificationAvailable()) {
            //TODO put arguments of initiative
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("detalle");
            Bundle bundle = new Bundle();
            bundle.putString(titulo, "Iniciativa nueva");
            bundle.putString(detalle, "El usuario tal ha creado una iniciativa");

            requestNotification(titulo, detalle, bundle);
        }
    }

    /**
     * Set notification
     * @param titulo
     * @param detalle
     * @param bundle
     */
    private void requestNotification(String titulo, String detalle, Bundle bundle) {
        String id = "mensaje";

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(this, id);
        NotificationChannel nc = new NotificationChannel(id, "nuevo", NotificationManager.IMPORTANCE_HIGH);
        nc.setShowBadge(true);
        if(nm!=null)
            nm.createNotificationChannel(nc);

        PendingIntent pendingIntent = new NavDeepLinkBuilder(JointlyApplication.getContext())
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.showInitiativeFragment)
                .setArguments(bundle)
                .createPendingIntent();

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle(titulo)
                .setContentText(detalle)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setContentIntent(pendingIntent);

        if (nm != null) {
            nm.notify(new Random().nextInt(1000), builder.build());
        }
    }

    /**
     * send notification
     */
    public static void sendNotification(String t) {
        if(t.equals(ATOPICO)) {
            generateNotificationToSelectUser("");
        }
        if(t.equals(TOPICO)) {
            generateNotificationToTopic();
        }
    }

    /**
     * generate the notification for new initiative user create
     * for notificate user follows him
     */
    private static void generateNotificationToTopic() {
        JSONObject jsonObject = new JSONObject();
        RequestQueue myrequest = Volley.newRequestQueue(JointlyApplication.getContext());

        try {
            jsonObject.put("to", "/topics/"+ FirebaseAuth.getInstance().getCurrentUser().getEmail());
            JSONObject notification = new JSONObject();
            notification.put("titulo", "iniciativa nueva");
            notification.put("detalle", "esto es el detalla");

            jsonObject.put("data", notification);

            String url = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, null,  null) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization", "key=AAAAUJplwQA:APA91bEQFiaSiIOJZTdd4t7d_J70nJE37Q1AhR_hQ3PS33s1iOOdkha8tA5VQ1-n_cFby0w0qunICE8ZC_xLTKwC_wydEGQ_lFAysPs3lK4HvdWTRkoT3OZhHcnqiLyQx61e0X_6ykYM");
                    return header;
                }
            };

            myrequest.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * generate the notification for a select action
     */
    private static void generateNotificationToSelectUser(String t) {
        JSONObject jsonObject = new JSONObject();
        RequestQueue myrequest = Volley.newRequestQueue(JointlyApplication.getContext());

        try {
            String token = t;
            jsonObject.put("to", token);
            JSONObject notification = new JSONObject();
            notification.put("titulo", "iniciativa nueva");
            notification.put("detalle", "esto es el detalla");

            jsonObject.put("data", notification);

            String url = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, null,  null) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization", "key=AAAAUJplwQA:APA91bEQFiaSiIOJZTdd4t7d_J70nJE37Q1AhR_hQ3PS33s1iOOdkha8tA5VQ1-n_cFby0w0qunICE8ZC_xLTKwC_wydEGQ_lFAysPs3lK4HvdWTRkoT3OZhHcnqiLyQx61e0X_6ykYM");
                    return header;
                }
            };

            myrequest.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
