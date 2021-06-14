package com.douglas.jointlyapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.login.LoginActivity;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.sync.SyncFromAPI;
import com.douglas.jointlyapp.ui.sync.SyncToAPI;

/**
 * Activity launcher
 */
public class SplashActivity extends AppCompatActivity {

    private static final long WAIT_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    /**
     * En este metodo se debe ejecutar todas las operaciones de inicio de la aplicacion
     * como conectarse a una base de datos, a un servicior... Simulamos el tiempo de espera
     * con un hilo que duerme 2 segundos y cuando despierta ejecuta el metodo initLogin
     */
    @Override
    protected void onStart() {
        super.onStart();

        new Thread(() -> {
            new Thread(() -> {
                SyncToAPI syncToAPI = new SyncToAPI(() -> null);
                syncToAPI.run();
                SyncFromAPI syncFromAPI = new SyncFromAPI(() -> null);
                syncFromAPI.run();
            });

            //TODO Volver a activar por si la sync no funciona
//            if(!JointlyApplication.getConnection()) {
//                initNoConnectionActivity();
//                return;
//            }

            if(!JointlyPreferences.getInstance().getRemember())
                initLogin();
            else {
                runOnUiThread(() -> JointlyApplication.setCurrentSignInUser(UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser())));
                initJointlyApp();
            }
        }).start();
    }

    /**
     * display NoConnectionActivity
     */
    private void initNoConnectionActivity() {
        startActivity(new Intent(this, NoConnectionActivity.class));

        finish();
    }

    /**
     * display Login
     */
    private void initLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        // Vamos a llamar de forma explicita al metodo finish() que destruye la activity
        // y no se muestra cuando se pulse el boton back
        finish();
    }

    /**
     * display JointlyActivity
     */
    private void initJointlyApp() {
        startActivity(new Intent(this, JointlyActivity.class));

        finish();
    }
}