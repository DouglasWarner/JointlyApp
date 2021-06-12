package com.douglas.jointlyapp.ui.profile;

import android.net.Uri;

import com.douglas.jointlyapp.data.model.User;

/**
 * Entity that connects within view and interactor
 */
public class ProfilePresenter implements ProfileContract.Presenter, ProfileInteractorImpl.ProfileInteractor {

    private ProfileContract.View view;
    private ProfileInteractorImpl interactor;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        interactor = new ProfileInteractorImpl(this);
    }

    @Override
    public void loadUser(String userEmail) {
        interactor.loadUser(userEmail);
    }

    @Override
    public void updateImage(User user, Uri image) {
        interactor.updateImage(user, image);
    }

    @Override
    public void onLoadUser(User user) {
        view.onLoadUser(user);
    }

    @Override
    public void onRatingUser(float average) {
        view.setRatingUser(average);
    }

    @Override
    public void onUpdateImage() {
        view.setUpdateImage();
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void loadRatingUser(String user) {
        interactor.loadRatingUser(user);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }
}
