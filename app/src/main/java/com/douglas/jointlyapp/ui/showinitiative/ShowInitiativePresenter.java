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
    public void loadInitiative(int idInitiative) {
        interactor.loadInitiative(idInitiative);
    }

    @Override
    public void joinInitiative(Initiative initiative) {
        interactor.joinInitiative(initiative);
    }

    @Override
    public void unJoinInitiative(Initiative initiative) {
        interactor.unJoinInitiative(initiative);
    }

    @Override
    public void loadListUserJoined(int idInitiative) {
        interactor.loadListUserJoined(idInitiative);
    }

    @Override
    public void loadUserStateJoined(int idInitiative) {
        interactor.loadUserStateJoined(idInitiative);
    }

    @Override
    public void loadUserOwner(int idInitiative) {
        interactor.loadUserOwner(idInitiative);
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
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
    public void onLoadListUserJoined(List<User> userList) {
        view.setLoadListUserJoined(userList);
    }

    @Override
    public void onLoadUserOwner(User user) {
        view.setLoadUserOwner(user);
    }

    @Override
    public void onSuccessLoad(Initiative initiative) {
        view.onSuccessLoad(initiative);
    }
}
