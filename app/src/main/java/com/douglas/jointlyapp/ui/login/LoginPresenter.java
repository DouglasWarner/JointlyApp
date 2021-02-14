package com.douglas.jointlyapp.ui.login;

import com.douglas.jointlyapp.ui.base.BasePresenter;

public class LoginPresenter implements LoginContract.Presenter, LoginInteractorImpl.LoginInteractor {

    private LoginContract.View view;
    private LoginInteractorImpl interactor;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        interactor = new LoginInteractorImpl(this);
    }

    @Override
    public void validateCredentialsUser(String email, String password) {
        view.showProgress();
        interactor.validateCredentials(email, password);
    }

    @Override
    public void onEmailEmptyError() {
        view.hideProgress();
        view.setEmailEmptyError();
    }

    @Override
    public void onPasswordEmptyError() {
        view.hideProgress();
        view.setPasswordEmptyError();
    }

    @Override
    public void onEmailFormatError() {
        view.hideProgress();
        view.setEmailFormatError();
    }

    @Override
    public void onPasswordFormatError() {
        view.hideProgress();
        view.setPasswordFormatError();
    }

    @Override
    public void onAuthenticationError() {
        view.hideProgress();
        view.setAuthenticationError();
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.onSuccess();
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }
}
