package com.douglas.jointlyapp.ui.signup;

/**
 * Entity that connects within view and interactor
 */
public class SignUpPresenter implements SignUpContract.Presenter, SignUpInteractorImpl.SignUpInteractor {

    private SignUpContract.View view;
    private SignUpInteractorImpl signUpInteractor;

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
        signUpInteractor = new SignUpInteractorImpl(this);
    }

    @Override
    public void addUser(String email, String password, String confirmPassword, String userName) {
        view.showProgressDialog();
        signUpInteractor.addUser(email, password, confirmPassword, userName);
    }

    @Override
    public void onEmailEmptyError() {
        view.hideProgressDialog();
        view.setEmailEmptyError();
    }

    @Override
    public void onPasswordEmptyError() {
        view.hideProgressDialog();
        view.setPasswordEmptyError();
    }

    @Override
    public void onUserNameEmptyError() {
        view.hideProgressDialog();
        view.setUserNameEmptyError();
    }

    @Override
    public void onEmailFormatError() {
        view.hideProgressDialog();
        view.setEmailFormatError();
    }

    @Override
    public void onPasswordFormatError() {
        view.hideProgressDialog();
        view.setPasswordFormatError();
    }

    @Override
    public void onPasswordNotEqualError() {
        view.hideProgressDialog();
        view.setPasswordNotEqualError();
    }

    @Override
    public void onUserExistsError() {
        view.hideProgressDialog();
        view.setUserExistsError();
    }

    @Override
    public void onSuccess() {
        view.hideProgressDialog();
        view.onSuccess();
    }

    @Override
    public void onError(String message) {
        view.hideProgressDialog();
        view.onError(message);
    }

    @Override
    public void onDestroy() {
        view = null;
        signUpInteractor = null;
    }
}
