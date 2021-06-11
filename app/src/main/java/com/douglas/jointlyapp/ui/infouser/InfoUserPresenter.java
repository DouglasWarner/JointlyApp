package com.douglas.jointlyapp.ui.infouser;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.List;

public class InfoUserPresenter implements InfoUserContract.Presenter, InfoUserInteractorImpl.ProfileInteractor {

    private InfoUserContract.View view;
    private InfoUserInteractorImpl interactor;

    public InfoUserPresenter(InfoUserContract.View view) {
        this.view = view;
        interactor = new InfoUserInteractorImpl(this);
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
    public void onCountUserFollow(long count) {
        view.setCountUserFollow(count);
    }

    @Override
    public void onCountUserParticipate(long count) {
        view.setCountUserParticipate(count);
    }

    @Override
    public void onSuccessListCreated(List<Initiative> listInitiativesCreated) {
        view.onSuccessListCreated(listInitiativesCreated);
    }

    @Override
    public void onSuccessListJoined(List<Initiative> listInitiativesJoined) {
        view.onSuccessListJoined(listInitiativesJoined);
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
    public void loadCountParticipate(User user) {
        interactor.loadCountUserParticipate(user);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }
}
