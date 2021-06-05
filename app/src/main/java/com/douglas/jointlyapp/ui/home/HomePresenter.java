package com.douglas.jointlyapp.ui.home;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter, HomeInteractorImpl.ListInitiativeInteractor {

    private HomeContract.View view;
    private HomeInteractorImpl interactor;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        interactor = new HomeInteractorImpl(this);
    }

    @Override
    public void load() {
        view.showProgress();
        interactor.loadData();
    }

    @Override
    public void syncData() {
        interactor.syncData();
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
    public void onSuccess(List<Initiative> list, List<User> userOwners) {
        view.hideProgress();
        view.onSuccess(list, userOwners);
    }

    @Override
    public void onError(String message) {
        view.showOnError(message);
    }
}
