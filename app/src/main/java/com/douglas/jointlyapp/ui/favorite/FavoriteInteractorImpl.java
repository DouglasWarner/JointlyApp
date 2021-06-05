package com.douglas.jointlyapp.ui.favorite;

import android.os.Handler;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.List;

public class FavoriteInteractorImpl {

    interface FavoriteInteractor
    {
        void onNoData();
        void onSuccess(List<User> list);
        void onSuccessUnFollow();
        void onSuccessFollow();
    }

    private FavoriteInteractorImpl.FavoriteInteractor interactor;

    public FavoriteInteractorImpl(FavoriteInteractorImpl.FavoriteInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData()
    {
        new Handler().postDelayed(() -> {
            User user = UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser());

            List<User> listUserFollowed = UserRepository.getInstance().getListUserFollowed(user.getEmail());

            if (listUserFollowed.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccess(listUserFollowed);
            }
        },500);
    }

    public void followUser(final User userFollowed)
    {
        User user = UserRepository.getInstance().getUser(JointlyPreferences.getInstance().getUser());

        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user.getEmail(), userFollowed.getEmail());

        if(userFollowUser != null)
        {
            UserRepository.getInstance().deleteUserFollowed(userFollowUser);
            interactor.onSuccessUnFollow();
        }
        else
        {
            UserRepository.getInstance().insertUserFollowed(new UserFollowUser(user.getEmail(), userFollowed.getEmail()));
            interactor.onSuccessFollow();
        }
    }
}
