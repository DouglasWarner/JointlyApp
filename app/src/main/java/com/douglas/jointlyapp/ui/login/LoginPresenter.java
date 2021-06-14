package com.douglas.jointlyapp.ui.login;

import android.app.Activity;
import android.view.animation.LinearInterpolator;

import com.douglas.jointlyapp.data.model.User;
import com.facebook.AccessToken;

/**
 * Entity that connects within view and interactor
 */
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
    public void doLoginGoogle(String idToken, Activity view) {
        interactor.doLoginGoogle(idToken, view);
    }

    @Override
    public void doLoginFacebook(AccessToken accessToken, Activity view) {
        interactor.doLoginFacebook(accessToken, view);
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
    public void onAuthenticationError(String message) {
        view.hideProgress();
        view.setAuthenticationError(message);
    }

    @Override
    public void onSuccess() {
        view.hideProgress();
        view.onSuccess();
    }

    @Override
    public void onError(String message) {
        view.hideProgress();
        view.onError(message);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }
}
