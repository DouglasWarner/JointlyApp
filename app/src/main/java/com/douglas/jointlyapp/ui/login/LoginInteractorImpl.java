package com.douglas.jointlyapp.ui.login;

import android.os.Handler;
import android.text.TextUtils;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

public class LoginInteractorImpl {

    public interface LoginInteractor {
        void onEmailEmptyError();
        void onPasswordEmptyError();
        void onEmailFormatError();
        void onPasswordFormatError();
        void onAuthenticationError();
        void onSuccess(User user);
    }

    private LoginInteractor interactor;

    public LoginInteractorImpl(LoginInteractor loginInteractor) {
        this.interactor = loginInteractor;
    }

    public void validateCredentials(final String email, final String password)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(email))
                {
                    interactor.onEmailEmptyError();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    interactor.onPasswordEmptyError();
                    return;
                }
                if(!CommonUtils.isEmailValid(email))
                {
                    interactor.onEmailFormatError();
                    return;
                }
                if(!CommonUtils.isPasswordValid(password))
                {
                    interactor.onPasswordFormatError();
                    return;
                }

                if(!UserRepository.getInstance().validateCredentials(email, password))
                {
                    interactor.onAuthenticationError();
                    return;
                }

                interactor.onSuccess(UserRepository.getInstance().getUser(email));

            }
        }, 2000);
    }
}
