package com.douglas.jointlyapp.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.douglas.jointlyapp.R;

public class JointlyPreferences {

    private static final String KEY_USER = "userEmail";
    private static final String KEY_PASSWORD = "password";
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
     * @param password
     * @return true si se realizo correctamente
     */
    public boolean putUser(String user, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(context.getResources().getString(R.string.key_user), user);
        editor.putString(context.getResources().getString(R.string.key_password), password);
        editor.commit();

        return true;

    }
}
