package com.douglas.jointlyapp.ui.home;

import android.os.Handler;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.List;

public class HomeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoData();
        void onSuccess(List<Initiative> list, List<User> userOwners);
    }

    private ListInitiativeInteractor interactor;

    public HomeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData()
    {
        new Handler().postDelayed(() -> {

            InitiativeRepository repository = InitiativeRepository.getInstance();
            String user = JointlyPreferences.getInstance().getUser();
            List<Initiative> list = repository.getList();
            List<User> userOwner = UserRepository.getInstance().getListInitiativeOwners();

            if (list.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccess(list, userOwner);
            }
        },500);
    }
}
