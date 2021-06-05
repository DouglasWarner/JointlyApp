package com.douglas.jointlyapp.ui.showinitiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseView;

import java.util.List;

public interface ShowInitiativeContract {

    interface View
    {
        void setJoined();
        void setUnJoined();
        void setUserListEmpty();
        void setLoadListUserJoined(List<User> userList);
        void setLoadUserOwner(User user);
        void onSuccessLoad(Initiative initiative);
        void onError(String message);
    }

    interface Presenter extends BasePresenter
    {
        void joinInitiative(Initiative initiative);
        void unJoinInitiative(Initiative initiative);
        void loadListUserJoined(long idInitiative);
        void loadUserStateJoined(String email);
        void loadUserOwner(String email);
    }
}
