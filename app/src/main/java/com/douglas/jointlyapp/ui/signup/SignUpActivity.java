package com.douglas.jointlyapp.ui.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.douglas.jointlyapp.R;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.JointlyActivity;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity that hold the signup user of the app
 */
public class SignUpActivity extends AppCompatActivity implements SignUpContract.View{

    //region Variables

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

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
        setListeners();

        signUpPresenter = new SignUpPresenter(this);
    }

    /**
     * setListeners
     */
    private void setListeners() {
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
        tieConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilConfirmPassword.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tieUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilUserName.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * initUI
     */
    private void initUI() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        tilUserName = findViewById(R.id.tilUsername);

        tieEmail = findViewById(R.id.tieEmail);
        tiePassword = findViewById(R.id.tiePassword);
        tieConfirmPassword = findViewById(R.id.tieConfirmPassword);
        tieUserName = findViewById(R.id.tieUsername);
    }

    /**
     * signUp botton click
     * @param v
     */
    public void signUp(View v) {
        hideKeyboard(v);
        signUpPresenter.addUser(tieEmail.getText().toString(), tiePassword.getText().toString(), tieConfirmPassword.getText().toString(), tieUserName.getText().toString());
        clearErrors();
    }

    /**
     * clearErrors for inputtextfields
     */
    public void clearErrors() {
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilConfirmPassword.setErrorEnabled(false);
        tilUserName.setErrorEnabled(false);
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
    public void setUserNameEmptyError() {
        tilUserName.setError(getString(R.string.error_user_name_empty));
        showKeyboard(tieUserName);
    }

    @Override
    public void setUserExistsError() {
        Snackbar.make(findViewById(R.id.signUpContent), R.string.error_user_exists, Snackbar.LENGTH_LONG).show();
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
    public void setPasswordNotEqualError() {
        tilConfirmPassword.setError(getString(R.string.error_confirm_password));
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
    public void onError(String message) {
        Toast.makeText(this, message!=null ? message : getString(R.string.error_register_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        JointlyPreferences.getInstance().putRemember(true);

        startActivity(new Intent(this, JointlyActivity.class));
        finish();
    }

    /**
     * show the keyboard
     * @param view
     */
    public void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imn = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imn.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * hide the keyboard
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
        signUpPresenter.onDestroy();
    }
}