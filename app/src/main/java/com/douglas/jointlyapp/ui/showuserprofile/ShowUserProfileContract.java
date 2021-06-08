package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

public interface ShowUserProfileContract {

    interface View {
        void setInitiativeCreatedEmpty();
        void setInitiativeJointedEmpty();
        void setSuccessUnFollow();
        void setSuccessFollow();
        void setUserStateFollow(boolean follow);
        void setCountUserFollow(long count);
        void setCountUserParticipate(long count);
        void onSuccess(List<Initiative> listInitiativesCreated, List<Initiative> listInitiativesJoined);
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadListInitiative(User user);
        void loadCountUserFollow(User user);
        void loadUserStateFollow(User user);
        void manageFollowUser(User user);
    }
}
