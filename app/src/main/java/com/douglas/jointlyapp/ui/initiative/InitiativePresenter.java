package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

public class InitiativePresenter implements InitiativeContract.Presenter, InitiativeInteractorImpl.ListInitiativeInteractor {

    private InitiativeContract.View view;
    private InitiativeInteractorImpl interactor;

    public InitiativePresenter(InitiativeContract.View view) {
        this.view = view;
        interactor = new InitiativeInteractorImpl(this);
    }

    @Override
    public void loadCreated(String history) {
        view.showProgress();
        interactor.loadCreated(history);
    }

    @Override
    public void loadJoined(String history, int type) {
        view.showProgress();
        interactor.loadJoined(history, type);
    }

    @Override
    public void onNoDataCreatedInProgress() {
        view.hideProgress();
        view.onNoDataCreatedInProgress();
    }

    @Override
    public void onNoDataCreatedHistory() {
        view.hideProgress();
        view.onNoDataCreatedHistory();
    }

    @Override
    public void onNoDataJoinedInProgress() {
        view.hideProgress();
        view.onNoDataJoinedInProgress();
    }

    @Override
    public void onNoDataJoinedHistory() {
        view.hideProgress();
        view.onNoDataJoinedHistory();
    }

    @Override
    public void onSuccessCreatedInProgress(List<Initiative> list) {
        view.hideProgress();
        view.onSuccessCreatedInProgress(list);
    }

    @Override
    public void onSuccessJoinedInProgress(List<Initiative> list) {
        view.hideProgress();
        view.onSuccessJoinedInProgress(list);
    }

    @Override
    public void onSuccessCreatedHistory(List<Initiative> list) {
        view.hideProgress();
        view.onSuccessCreatedHistory(list);
    }

    @Override
    public void onSuccessJoinedHistory(List<Initiative> list) {
        view.hideProgress();
        view.onSuccessJoinedHistory(list);
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
