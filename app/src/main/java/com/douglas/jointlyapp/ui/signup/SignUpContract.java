package com.douglas.jointlyapp.ui.signup;

import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseView;

/**
 * Interface that set the call logic within view and presenter
 */
public interface SignUpContract {

    interface View extends BaseView {
        void setEmailEmptyError();
        void setPasswordEmptyError();
        void setUserNameEmptyError();
        void setEmailFormatError();
        void setPasswordFormatError();
        void setUserExistsError();
        void setPasswordNotEqualError();
        void showProgressDialog();
        void hideProgressDialog();
    }

    interface Presenter extends BasePresenter {
        void addUser(String email, String password, String confirmPassword, String userName);
    }
}
