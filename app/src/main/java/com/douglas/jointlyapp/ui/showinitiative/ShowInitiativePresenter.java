package com.douglas.jointlyapp.ui.showinitiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.List;

public class ShowInitiativePresenter implements ShowInitiativeContract.Presenter, ShowInitiativeInteractorImpl.ShowInitiativeInteractor {

    private ShowInitiativeContract.View view;
    private ShowInitiativeInteractorImpl interactor;

    public ShowInitiativePresenter(ShowInitiativeContract.View view) {
        this.view = view;
        interactor = new ShowInitiativeInteractorImpl(this);
    }

    @Override
    public void joinInitiative(Initiative initiative) {
        interactor.joinInitiative(initiative);
    }

    @Override
    public void loadListUserJoined(long idInitiative) {
        interactor.loadListUserJoined(idInitiative);
    }

    @Override
    public void loadUserStateJoined(String email, long idInitiative) {
        interactor.loadUserStateJoined(email, idInitiative);
    }

    @Override
    public void loadUserOwner(String email) {
        interactor.loadUserOwner(email);
    }

    @Override
    public void delete(Initiative initiative) {
        interactor.deleteInitiative(initiative);
    }

    @Override
    public void onUserListEmpty() {
        view.setUserListEmpty();
    }

    @Override
    public void onJoined() {
        view.setJoined();
    }

    @Override
    public void onUnJoined() {
        view.setUnJoined();
    }

    @Override
    public void onLoadUserStateJoined(boolean joined) {
        view.setLoadUserStateJoined(joined);
    }

    @Override
    public void onLoadListUserJoined(List<User> userList) {
        view.setLoadListUserJoined(userList);
    }

    @Override
    public void onLoadUserOwner(User user) {
        view.setLoadUserOwner(user);
    }

    @Override
    public void onCannotDeleted() {
        view.setCannotDeleted();
    }

    @Override
    public void onSuccessDeleted() {
        view.setSuccessDeleted();
    }

    @Override
    public void onError(String message) {
        view.onError(message);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }
}
