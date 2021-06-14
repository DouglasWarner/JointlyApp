package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;

import java.util.List;

/**
 * Entity that connects within view and interactor
 */
public class InitiativePresenter implements InitiativeContract.Presenter, InitiativeInteractorImpl.ListInitiativeInteractor {

    private InitiativeContract.View view;
    private InitiativeInteractorImpl interactor;

    public InitiativePresenter(InitiativeContract.View view) {
        this.view = view;
        interactor = new InitiativeInteractorImpl(this);
    }

    @Override
    public void setParticipate(String ref_code) {
        interactor.setParticipate(ref_code);
    }

    @Override
    public void loadCreated() {
        view.showProgress();
        interactor.loadCreated();
    }

    @Override
    public void loadJoined() {
        view.showProgress();
        interactor.loadJoined();
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
    public void onSuccessParticipate() {
        view.onSuccessParticipate();
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
