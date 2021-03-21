package com.douglas.jointlyapp.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyActivity;
import com.douglas.jointlyapp.ui.firebase.FirebaseLogin;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.signup.SignUpActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginContract.Presenter presenter;

    private String email;
    private String password;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText tieEmail;
    private TextInputEditText tiePassword;
    private CheckBox chbRemember;
    private Button btnLoginWithGoogle;
    private Button btnLoginWithFacebook;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        presenter = new LoginPresenter(this);

        btnLoginWithGoogle.setOnClickListener(v -> {
            doLoginGoogle();
            Toast.makeText(this, "google", Toast.LENGTH_SHORT).show();
        });
        btnLoginWithFacebook.setOnClickListener(v -> {
            doLoginFacebook();
            Toast.makeText(this, "facebook", Toast.LENGTH_SHORT).show();
        });
    }

    private void doLoginGoogle() {
        GoogleSignInOptions signOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signOptions);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, FirebaseLogin.REQUEST_CODE_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == FirebaseLogin.REQUEST_CODE_GOOGLE)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if(requestCode == FirebaseLogin.REQUEST_CODE_FACEBOOK)
        {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            Log.d("TAG", "firebaseAuthWithGoogle:" + googleSignInAccount.getEmail());
            //Update UI
            //TODO implementar guardar usuario en base de datos
//            FirebaseLogin.getInstance().firebaseAuthWithGoogle(googleSignInAccount.getIdToken(),this);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void doLoginFacebook()
    {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);

//                        FirebaseLogin.getInstance().firebaseAuthWithFacebook(loginResult.getAccessToken(), LoginActivity.this);
                    }
                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                        // ...
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError", error);
                        // ...
                    }
                });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseLogin.getInstance().getFirebaseAuth().getCurrentUser();
        if(currentUser != null)
            Log.d("TAG", "usuario logueado " + currentUser.getEmail());

    }

    private void initUI() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tieEmail = findViewById(R.id.tieEmail);
        tiePassword = findViewById(R.id.tiePassword);
        chbRemember = findViewById(R.id.chbRemember);
        btnLoginWithGoogle = findViewById(R.id.btnSignInGoogle);
        btnLoginWithFacebook = findViewById(R.id.btnSignInFacebook);
    }

    public void validateUser(View v)
    {
        email = tieEmail.getText().toString();
        password = tiePassword.getText().toString();

        hideKeyboard(v);

        clearErrors();

        presenter.validateCredentialsUser(email, password);
    }

    public void goToSignUp(View v)
    {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void setEmailEmptyError() {
        tilEmail.setError("Se requiere un email");
        showKeyboard(tieEmail);
    }

    @Override
    public void setPasswordEmptyError() {
        tilPassword.setError("Se requiere la contraseña");
        showKeyboard(tiePassword);
    }

    @Override
    public void setEmailFormatError() {
        tilEmail.setError("Email incorrecto");
        showKeyboard(tieEmail);
    }

    @Override
    public void setPasswordFormatError() {
        tilPassword.setError("Contraseña incorrecto");
        showKeyboard(tiePassword);
    }

    @Override
    public void showProgress() {
        findViewById(R.id.pbLoad).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.pbLoad).setVisibility(View.GONE);
    }

    @Override
    public void setAuthenticationError() {
        Snackbar.make(findViewById(R.id.loginContent), "El usuario no existe", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(User user) {

        JointlyPreferences.getInstance().putUser(user.getEmail(), user.getName(), user.getLocation(), user.getPhone(), user.getDescription());
        JointlyPreferences.getInstance().putRemember(chbRemember.isChecked());

        startActivity(new Intent(LoginActivity.this, JointlyActivity.class));

        finish();
    }

    public void clearErrors()
    {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    public void showKeyboard(View view)
    {
        view.requestFocus();
        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideKeyboard(View view)
    {
        view.requestFocus();
        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}