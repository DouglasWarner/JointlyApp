package com.douglas.jointlyapp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.JointlyActivity;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.signup.SignUpActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private LoginContract.Presenter presenter;

    private String email;
    private String password;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText tieEmail;
    private TextInputEditText tiePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tieEmail = findViewById(R.id.tieEmail);
        tiePassword = findViewById(R.id.tiePassword);

        presenter = new LoginPresenter(this);
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
    public void onSuccess() {

        JointlyPreferences.getInstance().putUser(tieEmail.getText().toString(), tiePassword.getText().toString());

        startActivity(new Intent(this, JointlyActivity.class));

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