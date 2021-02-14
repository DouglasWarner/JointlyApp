package com.douglas.jointlyapp.ui.favorite;

import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.UserRepository;

import java.util.List;

public class FavoriteInteractorImpl {

    interface FavoriteInteractor
    {
        void onNoData();
        void onSuccess(List<User> list);
    }

    private FavoriteInteractorImpl.FavoriteInteractor interactor;

    public FavoriteInteractorImpl(FavoriteInteractorImpl.FavoriteInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData(User user)
    {
        UserRepository repository = UserRepository.getInstance();
        List<User> list = repository.getList();

        List<User> listUserFollowed = list.get(list.indexOf(user)).getUserFollowed();

        if (list.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(listUserFollowed);
        }
    }
}
