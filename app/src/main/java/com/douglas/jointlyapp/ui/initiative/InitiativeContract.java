package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

public interface InitiativeContract {
    interface View
    {
        void setNoData();
        void showProgress();
        void hideProgress();
        void onSuccessCreatedInProgress(List<Initiative> initiativeList);
        void onSuccessJoinedInProgress(List<Initiative> initiativeList);
        void onSuccessCreatedHistory(List<Initiative> initiativeList);
        void onSuccessJoinedHistory(List<Initiative> initiativeList);
    }

    interface Presenter extends BasePresenter
    {
        void loadCreatedInProgress();
        void loadJoinedInProgress();
        void loadCreatedHistory();
        void loadJoinedHistory();
    }
}
