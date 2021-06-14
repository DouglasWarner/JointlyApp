package com.douglas.jointlyapp.ui.login;

import android.app.Activity;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.facebook.AccessToken;

/**
 * Interface that set the call logic within view and presenter
 */
public interface LoginContract {

    interface View {
        void setEmailEmptyError();
        void setPasswordEmptyError();
        void setEmailFormatError();
        void setPasswordFormatError();

        void showProgress();
        void hideProgress();

        void setAuthenticationError(String message);
        void onError(String message);

        void onSuccess();
    }

    interface Presenter extends BasePresenter {
        void validateCredentialsUser(String email, String password);
        void doLoginGoogle(String idToken, Activity activity);
        void doLoginFacebook(AccessToken accessToken, Activity activity);
    }
}
