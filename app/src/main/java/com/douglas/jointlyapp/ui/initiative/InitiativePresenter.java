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
    public void loadCreatedInProgress() {
        view.showProgress();
        interactor.loadInitiativeCreatedInProgress();
    }

    @Override
    public void loadJoinedInProgress() {
        view.showProgress();
        interactor.loadInitiativeJoinedInProgress();
    }

    @Override
    public void loadCreatedHistory() {
        view.showProgress();
        interactor.loadInitiativeCreatedHistory();
    }

    @Override
    public void loadJoinedHistory() {
        view.showProgress();
        interactor.loadInitiativeJoinedHistory();
    }

    @Override
    public void onDestroy() {
        view = null;
        interactor = null;
    }

    @Override
    public void onNoData() {
        view.hideProgress();
        view.setNoData();
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
    public void onSuccessJoinedInHistory(List<Initiative> list) {
        view.hideProgress();
        view.onSuccessJoinedHistory(list);
    }
}
