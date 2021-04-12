package com.douglas.jointlyapp.ui.home;

import android.os.Handler;
import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoData();
        void onSuccess(List<Initiative> list, List<User> userOwners);
    }

    private ListInitiativeInteractor interactor;
    private InitiativeService initiativeService;

    public HomeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
    }

    public void loadData()
    {
        Call<List<Initiative>> call = initiativeService.getInitiative();

        call.enqueue(new Callback<List<Initiative>>() {
            @Override
            public void onResponse(Call<List<Initiative>> call, Response<List<Initiative>> response) {
                interactor.onSuccess(response.body(), null);
            }

            @Override
            public void onFailure(Call<List<Initiative>> call, Throwable t) {
                interactor.onNoData();
                Log.e("ERR", t.getMessage());
            }
        });

//        new Handler().postDelayed(() -> {
//
//            InitiativeRepository repository = InitiativeRepository.getInstance();
//            String user = JointlyPreferences.getInstance().getUser();
//            List<Initiative> list = repository.getList();
//            List<User> userOwner = UserRepository.getInstance().getListInitiativeOwners();
//
//            if (list.isEmpty()) {
//                interactor.onNoData();
//            } else {
//                interactor.onSuccess(list, userOwner);
//            }
//        },500);
    }
}
