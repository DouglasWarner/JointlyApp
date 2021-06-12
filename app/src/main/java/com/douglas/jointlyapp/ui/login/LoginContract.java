package com.douglas.jointlyapp.ui.login;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

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

        void setAuthenticationError();

        void onSuccess(User user);
    }

    interface Presenter extends BasePresenter {
        void validateCredentialsUser(String email, String password);
    }
}
