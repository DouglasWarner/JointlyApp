package com.douglas.jointlyapp.ui.showuserprofile;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.List;

public class ShowUserProfileInteractorImpl {

    interface ProfileInteractor
    {
        void onLocationEmpty();
        void onPhoneEmpty();
        void onDescriptionEmpty();
        void onUserFollowersEmpty();
        void onInitiativeCreatedEmpty();
        void onInitiativeJointedEmpty();
        void onInitiativeInProgressEmptyError();
        void onUserFollowed();
        void onSuccess(User user, int countUserFollowers, int initiativeCreated, int initiativeJoined);
        void onSuccessInitiativeInProgress(List<Initiative> initiatives);
        void onSuccessUnFollow();
        void onSuccessFollow();
    }

    private ProfileInteractor interactor;

    public ShowUserProfileInteractorImpl(ProfileInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadUser(final String userEmail)
    {
        User user = UserRepository.getInstance().getUser(userEmail);
        int initiativeCreated = InitiativeRepository.getInstance().getListUserCreated(userEmail).size();
        int initiativeJoined = InitiativeRepository.getInstance().getListUserJoined(userEmail).size();
        int userFollowers = UserRepository.getInstance().getCountUserFollowers(userEmail);
        List<String> follows = UserRepository.getInstance().getListUserFollows(JointlyPreferences.getInstance().getUser());

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
        if(follows.contains(userEmail))
            interactor.onUserFollowed();

        interactor.onSuccess(user, userFollowers, initiativeCreated, initiativeJoined);
    }

    public void loadListInitiativeInProgress(final String userEmail)
    {
        List<Initiative> list = InitiativeRepository.getInstance().getListCreatedByUser(userEmail);

        if(list.isEmpty()) {
            interactor.onInitiativeInProgressEmptyError();
            return;
        }

        interactor.onSuccessInitiativeInProgress(list);
    }

    public void followUser(final String userFollowed)
    {
        User user = UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser());

        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user.getEmail(), userFollowed);

        if(userFollowUser != null)
        {
            UserRepository.getInstance().deleteUserFollowed(userFollowUser);
            interactor.onSuccessUnFollow();
        }
        else
        {
            UserRepository.getInstance().insertUserFollowed(new UserFollowUser(user.getEmail(), userFollowed));
            interactor.onSuccessFollow();
        }

    }
}
