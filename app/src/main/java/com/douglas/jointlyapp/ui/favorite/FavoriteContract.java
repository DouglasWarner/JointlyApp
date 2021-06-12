package com.douglas.jointlyapp.ui.favorite;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

/**
 * Interface that set the call logic within view and presenter
 */
public interface FavoriteContract {

    interface View extends BaseListView<User> {
        void setNoData();
        void showProgress();
        void hideProgress();
        void setSuccessUnFollow();
        void setSuccessFollow();
    }

    interface Presenter extends BasePresenter {
        void load();
        void followUser(User user);
    }
}
