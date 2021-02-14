package com.douglas.jointlyapp.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.ui.JointlyActivity;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity implements SignUpContract.View{

    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputLayout tilUserName;
    private TextInputEditText tieEmail;
    private TextInputEditText tiePassword;
    private TextInputEditText tieConfirmPassword;
    private TextInputEditText tieUserName;

    private ProgressDialog progressDialog;

    private SignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tilUserName = findViewById(R.id.tilUsername);

        tieEmail = findViewById(R.id.tieEmail);
        tiePassword = findViewById(R.id.tiePassword);
        tieConfirmPassword = findViewById(R.id.tieConfirmPassword);
        tieUserName = findViewById(R.id.tieUsername);



        signUpPresenter = new SignUpPresenter(this);
    }

    public void signUp(View v)
    {
        signUpPresenter.addUser(tieEmail.getText().toString(), tiePassword.getText().toString(),tieConfirmPassword.getText().toString(), tieUserName.getText().toString());

        clearErrors();
    }

    public void clearErrors()
    {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilConfirmPassword.setErrorEnabled(false);
        tilUserName.setErrorEnabled(false);
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
    public void setUserNameEmptyError() {
        tilUserName.setError("Se requiere un nombre de usuario");
        showKeyboard(tieUserName);
    }

    @Override
    public void setUserExistsError() {
        Snackbar.make(findViewById(R.id.signUpContent), "El usuario existe", Snackbar.LENGTH_LONG).show();
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
    public void setPasswordNotEqualError() {
        tilConfirmPassword.setError("Las contraseñas deben ser iguales");
    }

    @Override
    public void showProgressDialog() {
        progressDialog = CommonUtils.showLoadingDialog(this);
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this, JointlyActivity.class));

        finish();
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
        signUpPresenter.onDestroy();
    }
}