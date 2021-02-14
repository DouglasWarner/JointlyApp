package com.douglas.jointlyapp.ui.login;

import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseView;

public interface LoginContract {

    interface View extends BaseView
    {
        void setEmailEmptyError();
        void setPasswordEmptyError();
        void setEmailFormatError();
        void setPasswordFormatError();

        void showProgress();
        void hideProgress();

        void setAuthenticationError();
    }

    interface Presenter extends BasePresenter
    {
        void validateCredentialsUser(String email, String password);
    }
}
