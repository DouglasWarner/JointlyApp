package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.List;

public class ShowUserProfilePresenter implements ShowUserProfileContract.Presenter, ShowUserProfileInteractorImpl.ProfileInteractor {

    private ShowUserProfileContract.View view;
    private ShowUserProfileInteractorImpl interactor;

    public ShowUserProfilePresenter(ShowUserProfileContract.View view) {
        this.view = view;
        interactor = new ShowUserProfileInteractorImpl(this);
    }

    @Override
    public void loadUser(String userEmail) {
        interactor.loadUser(userEmail);
    }

    @Override
    public void loadListInitiativeInProgress(String userEmail) {
        interactor.loadListInitiativeInProgress(userEmail);
    }

    @Override
    public void followUser(String user) {
        interactor.followUser(user);
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
    public void onInitiativeInProgressEmptyError() {
        view.setInitiativeInProgressEmptyError();
    }

    @Override
    public void onUserFollowed() {
        view.setUserFollowed();
    }

    @Override
    public void onSuccess(User user, int countUserFollowers, int initiativeCreated, int initiativeJoined) {
        view.onSuccess(user, countUserFollowers, initiativeCreated, initiativeJoined);
    }

    @Override
    public void onSuccessInitiativeInProgress(List<Initiative> initiatives) {
        view.onSuccess(initiatives);
    }

    @Override
    public void onSuccessUnFollow() {
        view.setSuccessUnFollow();
    }

    @Override
    public void onSuccessFollow() {
        view.setSuccessFollow();
    }
}
