package com.douglas.jointlyapp.ui.showinitiative;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

import java.util.List;

public interface ShowInitiativeContract {

    interface View {
        void setJoined();
        void setUnJoined();
        void setUserListEmpty();
        void setLoadUserStateJoined(boolean joined);
        void setLoadListUserJoined(List<User> userList);
        void setLoadUserOwner(User user);

        void setCannotDeleted();
        void setSuccessDeleted();
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void joinInitiative(Initiative initiative);
        void loadListUserJoined(long idInitiative);
        void loadUserStateJoined(String email, long idInitiative);
        void loadUserOwner(String email);
        void delete(Initiative initiative);
    }
}
