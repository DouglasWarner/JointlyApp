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
    public void load() {
        view.showProgress();
        interactor.loadData();
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
    public void onSuccess(List<Initiative> list) {
        view.hideProgress();
        view.onSuccess(list);
    }
}
