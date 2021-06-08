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
    public void onDestroy() {
        view = null;
        interactor = null;
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
    public void onSuccessUnFollow() {
        view.setSuccessUnFollow();
    }

    @Override
    public void onSuccessFollow() {
        view.setSuccessFollow();
    }

    @Override
    public void onUserStateFollow(boolean follow) {
        view.setUserStateFollow(follow);
    }

    @Override
    public void onCountUserFollow(long count) {
        view.setCountUserFollow(count);
    }

    @Override
    public void onCountUserParticipate(long count) {
        view.setCountUserParticipate(count);
    }

    @Override
    public void onSuccess(List<Initiative> listInitiativesCreated, List<Initiative> listInitiativesJoined) {
        view.onSuccess(listInitiativesCreated, listInitiativesJoined);
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void loadListInitiative(User user) {
        interactor.loadListInitiative(user);
    }

    @Override
    public void loadCountUserFollow(User user) {
        interactor.loadCountUserFollow(user);
    }

    @Override
    public void loadUserStateFollow(User user) {
        interactor.loadUserStateFollow(user);
    }

    @Override
    public void manageFollowUser(User user) {
        interactor.manageFollowUser(user);
    }
}
