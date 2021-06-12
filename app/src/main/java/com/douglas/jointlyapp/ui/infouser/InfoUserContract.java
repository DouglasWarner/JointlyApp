package com.douglas.jointlyapp.ui.infouser;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

/**
 * Interface that set the call logic within view and presenter
 */
public interface InfoUserContract {

    interface View {
        void setInitiativeCreatedEmpty();
        void setInitiativeJointedEmpty();
        void setCountUserFollow(long count);
        void setCountUserParticipate(long count);
        void onSuccessListCreated(List<Initiative> listInitiativesCreated);
        void onSuccessListJoined(List<Initiative> listInitiativesJoined);
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadListInitiative(User user);
        void loadCountUserFollow(User user);
        void loadCountParticipate(User user);
    }
}
