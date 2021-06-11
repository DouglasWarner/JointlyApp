package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface ShowUserProfileContract {

    interface View {
        void setSuccessUnFollow();
        void setSuccessFollow();
        void setUserStateFollow(boolean follow);
        void setRatingUser(float average);
        void onError(String message);
    }

    interface Presenter extends BasePresenter {
        void loadRatingUser(User user);
        void loadUserStateFollow(User user);
        void manageFollowUser(User user);
    }
}
