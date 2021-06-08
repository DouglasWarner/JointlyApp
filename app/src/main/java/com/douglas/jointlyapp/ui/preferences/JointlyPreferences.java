package com.douglas.jointlyapp.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.douglas.jointlyapp.R;

public class JointlyPreferences {

    private static final String KEY_USER = "userEmail";
    private static final String KEY_REMEMBER = "userRemember";
    private static final String KEY_ORDERBY_INITIATIVE = "orderby_initiative";
    private static final String KEY_ORDERBY_FAVORITE = "orderby_favorite";
    private static final String KEY_NOTIFICATION = "notification";
    private Context context;
    private static JointlyPreferences instance;


    private JointlyPreferences(Context context) {
        this.context = context;
    }

    static public void newInstance(Context context) {
        if (instance == null)
            instance = new JointlyPreferences(context);
    }

    static public JointlyPreferences getInstance() {
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

    public String getUser() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(KEY_USER, "");
    }

    public boolean putRemember(boolean remember) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(context.getResources().getString(R.string.key_remember), remember);

        editor.commit();

        return true;
    }

    public boolean getRemember() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(KEY_REMEMBER, false);
    }

    public boolean getNotificationAvaible()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getBoolean(KEY_NOTIFICATION, false);
    }

    public String getOrderByInitiative()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Log.d("TAG", sharedPreferences.getString(KEY_ORDERBY_INITIATIVE, ""));
        return sharedPreferences.getString(KEY_ORDERBY_INITIATIVE, "");
    }

    public String getOrderByFavorite()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Log.d("TAG", sharedPreferences.getString(KEY_ORDERBY_FAVORITE, ""));
        return sharedPreferences.getString(KEY_ORDERBY_FAVORITE, "");
    }

    public void putOrderByInitiative(String s) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getResources().getString(R.string.key_orderby_initiative), s);

        editor.commit();
    }

    public void putOrderByFavorite(String s) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getResources().getString(R.string.key_orderby_favorite), s);

        editor.commit();
    }
}
