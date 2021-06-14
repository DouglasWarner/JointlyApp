package com.douglas.jointlyapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyActivity;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.signup.SignUpActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

/**
 * Activity manage login of app
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    //region Variables
    public static final int REQUEST_CODE_GOOGLE = 10000;

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

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setListeners();

        presenter = new LoginPresenter(this);
    }

    /**
     * setListeners
     */
    private void setListeners() {
        btnLoginWithGoogle.setOnClickListener(v -> {
            doLoginGoogle();
        });
        btnLoginWithFacebook.setOnClickListener(v -> {
            doLoginFacebook();
        });
        tieEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEmail.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tiePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * doLoginGoogle
     */
    private void doLoginGoogle() {
        GoogleSignInOptions signOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signOptions);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if(FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * handleSignInResult for google
     * @param task
     */
    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            Log.d("TAG", "firebaseAuthWithGoogle:" + googleSignInAccount.getEmail());
            presenter.doLoginGoogle(googleSignInAccount.getIdToken(), this);
        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    /**
     * doLoginFacebook
     */
    private void doLoginFacebook() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Collections.singleton("email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);
                        presenter.doLoginFacebook(loginResult.getAccessToken(), LoginActivity.this);
                    }
                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                    }
                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError", error);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
            Log.d("TAG", "usuario logueado " + currentUser.getEmail());

    }

    /**
     * initUI
     */
    private void initUI() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tieEmail = findViewById(R.id.tieEmail);
        tiePassword = findViewById(R.id.tiePassword);
        chbRemember = findViewById(R.id.chbRemember);
        btnLoginWithGoogle = findViewById(R.id.btnSignInGoogle);
        btnLoginWithFacebook = findViewById(R.id.btnSignInFacebook);
    }

    /**
     * validateUser
     * @param v
     */
    public void validateUser(View v) {
        email = tieEmail.getText().toString();
        password = tiePassword.getText().toString();
        hideKeyboard(v);
        clearErrors();
        presenter.validateCredentialsUser(email, password);
    }

    /**
     * goToSignUp
     * @param v
     */
    public void goToSignUp(View v)
    {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    public void setEmailEmptyError() {
        tilEmail.setError(getString(R.string.error_email_empty));
        showKeyboard(tieEmail);
    }

    @Override
    public void setPasswordEmptyError() {
        tilPassword.setError(getString(R.string.error_password_empty));
        showKeyboard(tiePassword);
    }

    @Override
    public void setEmailFormatError() {
        tilEmail.setError(getString(R.string.error_email_format));
        showKeyboard(tieEmail);
    }

    @Override
    public void setPasswordFormatError() {
        tilPassword.setError(getString(R.string.error_password_format));
        showKeyboard(tiePassword);
    }

    @Override
    public void showProgress() {
        findViewById(R.id.pbLoad).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        findViewById(R.id.pbLoad).setVisibility(View.INVISIBLE);
    }

    @Override
    public void setAuthenticationError(String message) {
        Snackbar.make(findViewById(R.id.loginContent), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message!=null ? message : getString(R.string.error_login_connection), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess() {
        JointlyPreferences.getInstance().putRemember(chbRemember.isChecked());
        startActivity(new Intent(LoginActivity.this, JointlyActivity.class));
        finish();
    }

    /**
     * clearErrors on textview
     */
    public void clearErrors() {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
    }

    /**
     * showKeyboard
     * @param view
     */
    public void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
    }

    /**
     * hideKeyboard
     * @param view
     */
    public void hideKeyboard(View view) {
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