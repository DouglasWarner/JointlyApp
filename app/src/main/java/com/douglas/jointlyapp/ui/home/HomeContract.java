package com.douglas.jointlyapp.ui.home;

import com.douglas.jointlyapp.data.model.HomeListAdapter;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

/**
 * Interface that set the call logic within view and presenter
 */
public interface HomeContract {

    interface View {
        void setNoData();
        void showProgress();
        void hideProgress();
        void onSuccess(List<HomeListAdapter> homeListAdapters);
        void showOnError(String message);
        void onSync();
    }

    interface Presenter extends BasePresenter {
        void load();
        void syncData();
    }
}
