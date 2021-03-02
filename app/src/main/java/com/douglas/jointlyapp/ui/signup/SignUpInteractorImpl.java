package com.douglas.jointlyapp.ui.signup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SignUpInteractorImpl {

    interface SignUpInteractor
    {
        void onEmailEmptyError();
        void onPasswordEmptyError();
        void onUserNameEmptyError();
        void onEmailFormatError();
        void onPasswordFormatError();
        void onPasswordNotEqualError();
        void onUserExistsError();
        void onSuccess();
    }

    private SignUpInteractor interactor;

    public SignUpInteractorImpl(SignUpInteractor signUpInteractor) {
        this.interactor = signUpInteractor;
    }

    public void addUser(final String email, final String password, final String confirmPassword, final String userName)
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(TextUtils.isEmpty(email))
                {
                    interactor.onEmailEmptyError();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    interactor.onPasswordEmptyError();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)){
                    interactor.onPasswordEmptyError();
                    return;
                }

                if(TextUtils.isEmpty(userName))
                {
                    interactor.onUserNameEmptyError();
                    return;
                }

                if (!CommonUtils.isEmailValid(email)){
                    interactor.onEmailFormatError();
                    return;
                }

                if (!CommonUtils.isPasswordValid(password)) {
                    interactor.onPasswordFormatError();
                    return;
                }

                if (!password.equals(confirmPassword)){
                    interactor.onPasswordNotEqualError();
                    return;
                }

                UserRepository repository = UserRepository.getInstance();

                if (repository.userExists(email)){
                    interactor.onUserExistsError();
                    return;
                }

                repository.insert(new User(email, password, userName, " ", CommonUtils.getImagenUserDefault(JointlyApplication.getContext()), " ", " ", CommonUtils.getDateNow()));

                interactor.onSuccess();

            }
        }, 2000);
    }
}
