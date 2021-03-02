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
    }

    interface Presenter extends BasePresenter
    {
        void loadInitiative(int idInitiative);
        void joinInitiative(Initiative initiative);
        void unJoinInitiative(Initiative initiative);
        void loadListUserJoined(int idInitiative);
        void loadUserStateJoined(int idInitiative);
        void loadUserOwner(int idInitiative);
    }
}
