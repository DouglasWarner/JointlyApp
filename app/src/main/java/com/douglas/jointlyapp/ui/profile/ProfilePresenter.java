package com.douglas.jointlyapp.ui.profile;

import com.douglas.jointlyapp.data.model.User;

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
    public void updateImage(User user) {
        interactor.updateUser(user);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }

    @Override
    public void onLocationEmpty() {
        view.setLocationEmpty();
    }

    @Override
    public void onPhoneEmpty() {
        view.setPhoneEmpty();
    }

    @Override
    public void onDescriptionEmpty() {
        view.setDescriptionEmpty();
    }

    @Override
    public void onUserFollowersEmpty() {
        view.setUserFollowersEmpty();
    }

    @Override
    public void onInitiativeCreatedEmpty() {
        view.setInitiativeCreatedEmpty();
    }

    @Override
    public void onInitiativeJointedEmpty() {
        view.setInitiativeJointedEmpty();
    }

    @Override
    public void onSuccess(User user, int countUserFollowers, int initiativeCreated, int initiativeJoined) {
        view.onSuccess(user, countUserFollowers, initiativeCreated, initiativeJoined);
    }
}
