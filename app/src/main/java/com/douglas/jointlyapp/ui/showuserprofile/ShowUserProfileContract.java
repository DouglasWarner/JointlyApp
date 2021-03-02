package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BaseListView;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseProfileView;

public interface ShowUserProfileContract {

    interface View extends BaseProfileView<User>, BaseListView<Initiative>
    {
        void setLocationEmpty();
        void setPhoneEmpty();
        void setDescriptionEmpty();
        void setUserFollowersEmpty();
        void setInitiativeCreatedEmpty();
        void setInitiativeJointedEmpty();
        void setInitiativeInProgressEmptyError();
        void setSuccessUnFollow();
        void setSuccessFollow();
        void setUserFollowed();
    }

    interface Presenter extends BasePresenter
    {
        void loadUser(String userEmail);

        void loadListInitiativeInProgress(String userEmail);

        void followUser(String userEmail);
    }
}
