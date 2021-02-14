package com.douglas.jointlyapp.ui.favorite;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseView;

public interface FavoriteContract {

    interface View extends BaseListView<User>
    {
        void setNoData();
        void showProgress();
        void hideProgress();
    }

    interface Presenter extends BasePresenter
    {
        void load(User user);
    }
}
