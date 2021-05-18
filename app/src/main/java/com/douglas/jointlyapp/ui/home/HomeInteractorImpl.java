package com.douglas.jointlyapp.ui.home;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.ui.utils.Apis;

import java.util.ArrayList;
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
        this.initiativeService = Apis.getInitiativeService();
    }

    public void loadData()
    {
        Call<APIResponse> call = initiativeService.getInitiative();

        call.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.e("TAG", response.message());
                if(response.body() != null)
                    interactor.onSuccess(response.body().getData(), new ArrayList<>());
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                interactor.onNoData();
                Log.e("ERR", t.getMessage());
            }
        });

//        call.enqueue(new Callback<List<Initiative>>() {
//            @Override
//            public void onResponse(Call<List<Initiative>> call, Response<List<Initiative>> response) {
//                if(response.body() != null)
//                    interactor.onSuccess(response.body(), null);
//            }
//
//            @Override
//            public void onFailure(Call<List<Initiative>> call, Throwable t) {
//                interactor.onNoData();
//                Log.e("ERR", t.getMessage());
//            }
//
//        });

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
