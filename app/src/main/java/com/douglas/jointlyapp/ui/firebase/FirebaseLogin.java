package com.douglas.jointlyapp.ui.firebase;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.login.LoginActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class FirebaseLogin {

    private static FirebaseLogin firebaseLogin;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    public static final int REQUEST_CODE_GOOGLE = 10000;
    public static final int REQUEST_CODE_FACEBOOK = 10001;

    private List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build()
    );

    static {
        firebaseLogin = new FirebaseLogin();
    }

    private FirebaseLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if (user != null) {
//                    Toast.makeText(JointlyApplication.getContext(), "Iniciaste sesion", Toast.LENGTH_SHORT).show();
//                } else {
//                    startActivityForResult(AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(provider)
//                            .setIsSmartLockEnabled(false)
//                            .build(), REQUEST_CODE);
//                }
//            }
//        };
    }

    public static FirebaseLogin getInstance()
    {
        return firebaseLogin;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void firebaseAuthWithGoogle(String idToken, Activity view)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(view, (OnCompleteListener<AuthResult>) task -> {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.getException());
                showSnackbar(view.findViewById(R.id.loginContent));
//                            updateUI(null);
            }
        });
    }

    public void firebaseAuthWithFacebook(AccessToken accessToken, Activity view)
    {
        Log.d("TAG", "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(view, (OnCompleteListener<AuthResult>) task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                            updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        Toast.makeText(view, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }

                    // ...
                });

    }

    private void showSnackbar(View view) {
        Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
    }
}
