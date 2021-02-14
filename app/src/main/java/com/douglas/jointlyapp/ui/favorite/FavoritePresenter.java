package com.douglas.jointlyapp.ui.favorite;

import com.douglas.jointlyapp.data.model.User;

import java.util.List;

public class FavoritePresenter implements FavoriteContract.Presenter, FavoriteInteractorImpl.FavoriteInteractor {
    private FavoriteContract.View view;
    private FavoriteInteractorImpl interactor;

    public FavoritePresenter(FavoriteContract.View view) {
        this.view = view;
        interactor = new FavoriteInteractorImpl(this);
    }

    @Override
    public void load(User user) {
        view.showProgress();
        interactor.loadData(user);
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
    public void onSuccess(List<User> list) {
        view.hideProgress();
        view.onSuccess(list);
    }
}
