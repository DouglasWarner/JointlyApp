package com.douglas.jointlyapp.ui.profile;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseProfileView;

public interface ProfileContract {

    interface View extends BaseProfileView<User>
    {
        void setLocationEmpty();
        void setPhoneEmpty();
        void setDescriptionEmpty();
        void setUserFollowersEmpty();
        void setInitiativeCreatedEmpty();
        void setInitiativeJointedEmpty();
    }

    interface Presenter extends BasePresenter
    {
        void loadUser(String userEmail);
        void updateImage(User user);
    }
}
