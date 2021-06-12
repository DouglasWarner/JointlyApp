package com.douglas.jointlyapp.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.douglas.jointlyapp.R;

/**
 * Entity that init all the preferences of app
 */
public class JointlyPreferences {

    private static final String KEY_USER = "userEmail";
    private static final String KEY_REMEMBER = "userRemember";
    private static final String KEY_ORDERBY_INITIATIVE = "orderby_initiative";
    private static final String KEY_NOTIFICATION = "notification";
    private static final String KEY_SYNC = "sync";
    private Context context;
    private static JointlyPreferences instance;

    private JointlyPreferences(Context context) {
        this.context = context;
    }

    public static void newInstance(Context context) {
        if (instance == null)
            instance = new JointlyPreferences(context);
    }

    public static JointlyPreferences getInstance() {
        return instance;
    }

    /**
     * Metodo que escribe en el fichero de preferencias la informacion del usuario
     *
     * @param user
     * @return true si se realizo correctamente
     */
    public boolean putUser(String user, String name, String location, String phone, String description) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.key_user), user);
        editor.putString(context.getResources().getString(R.string.key_name), name);
        editor.putString(context.getResources().getString(R.string.key_location), location);
        editor.putString(context.getResources().getString(R.string.key_phone), phone);
        editor.putString(context.getResources().getString(R.string.key_description), description);
        editor.commit();

        return true;
    }

    /**
     * get the current login user from local
     * @return String
     */
    public String getUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_USER, "");
    }

    /**
     * put the remember value user on login
     * @param remember
     * @return boolean
     */
    public boolean putRemember(boolean remember) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getResources().getString(R.string.key_remember), remember);
        editor.commit();

        return true;
    }

    /**
     * get the remember value user on login
     * @return boolean
     */
    public boolean getRemember() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_REMEMBER, false);
    }

    /**
     * get if notification is available
     * @return boolean
     */
    public boolean getNotificationAvailable() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(KEY_NOTIFICATION, false);
    }

    /**
     * get order initiative by default
     * @return String
     */
    public String getOrderByInitiative() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("TAG", sharedPreferences.getString(KEY_ORDERBY_INITIATIVE, ""));
        return sharedPreferences.getString(KEY_ORDERBY_INITIATIVE, "");
    }

    /**
     * set order initiative by default
     * @param s
     */
    public void putOrderByInitiative(String s) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.key_orderby_initiative), s);
        editor.commit();
    }

    /**
     * set if the app is sync to API
     * @param sync
     */
    public void putSync(String sync) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.key_sync), sync);
        editor.commit();
    }

    /**
     * get if the app is sync to API
     * @return String
     */
    public String getSync() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(KEY_SYNC, "");
    }
}
