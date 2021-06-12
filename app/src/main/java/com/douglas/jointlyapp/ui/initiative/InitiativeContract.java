package com.douglas.jointlyapp.ui.initiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

/**
 * Interface that set the call logic within view and presenter
 */
public interface InitiativeContract {

    interface View {
        void showProgress();
        void hideProgress();
        void onNoDataCreatedInProgress();
        void onNoDataCreatedHistory();
        void onNoDataJoinedInProgress();
        void onNoDataJoinedHistory();
        void onSuccessCreatedInProgress(List<Initiative> initiativeList);
        void onSuccessJoinedInProgress(List<Initiative> initiativeList);
        void onSuccessCreatedHistory(List<Initiative> initiativeList);
        void onSuccessJoinedHistory(List<Initiative> initiativeList);
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadCreated(String history);
        void loadJoined(String history, int type);
    }
}
