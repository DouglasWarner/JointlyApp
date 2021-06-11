package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.User;

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
    public void onRatingUser(float average) {
        view.setRatingUser(average);
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void loadRatingUser(User user) {
        interactor.loadRatingUser(user);
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
