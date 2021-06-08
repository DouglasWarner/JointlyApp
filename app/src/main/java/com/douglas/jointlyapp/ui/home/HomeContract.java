package com.douglas.jointlyapp.ui.home;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

public interface HomeContract {

    interface View {
        void setNoData();
        void showProgress();
        void hideProgress();
        void onSuccess(List<Initiative> list, List<User> userOwners, List<Long> countUsersJoined);
        void showOnError(String message);
        void onSync();
    }

    interface Presenter extends BasePresenter {
        void load();
        void syncData();
    }
}
