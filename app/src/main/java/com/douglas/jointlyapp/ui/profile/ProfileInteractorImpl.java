package com.douglas.jointlyapp.ui.profile;

import android.os.Handler;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;

public class ProfileInteractorImpl {

    interface ProfileInteractor
    {
        void onLocationEmpty();
        void onPhoneEmpty();
        void onDescriptionEmpty();
        void onUserFollowersEmpty();
        void onInitiativeCreatedEmpty();
        void onInitiativeJointedEmpty();
        void onSuccess(User user, long countUserFollowers, int initiativeCreated, int initiativeJoined);
    }

    private ProfileInteractor interactor;

    public ProfileInteractorImpl(ProfileInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadUser(final String userEmail) {
        User user = UserRepository.getInstance().getUser(userEmail);
        int initiativeCreated = InitiativeRepository.getInstance().getListCreatedByUser(userEmail, false).size();
        int initiativeJoined = InitiativeRepository.getInstance().getListJoinedByUser(userEmail, 1,false).size();
        long userFollowers = UserRepository.getInstance().getCountUserFollowers(userEmail);

        if(user.getLocation().isEmpty())
            interactor.onLocationEmpty();
        if(user.getPhone().isEmpty())
            interactor.onPhoneEmpty();
        if(user.getDescription().isEmpty())
            interactor.onDescriptionEmpty();
        if(userFollowers < 0)
            interactor.onUserFollowersEmpty();
        if(initiativeCreated < 0)
            interactor.onInitiativeCreatedEmpty();
        if(initiativeJoined < 0)
            interactor.onInitiativeJointedEmpty();

        interactor.onSuccess(user, userFollowers, initiativeCreated, initiativeJoined);
    }

    public void updateUser(final User user)
    {
        UserRepository.getInstance().update(user);
    }
}
