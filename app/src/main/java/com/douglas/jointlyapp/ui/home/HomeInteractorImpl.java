package com.douglas.jointlyapp.ui.home;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractorImpl {

    interface ListInitiativeInteractor {

        void onNoData();

        void onSuccess(List<Initiative> list, List<User> userOwners);

        void onError(String message);

        //TODO call for the count of users joined
        //TODO mirar todas las llamadas onError para mandar mensajes
    }

    private ListInitiativeInteractor interactor;
    private InitiativeService initiativeService;
    private UserService userService;

    public HomeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
        initiativeService = Apis.getInstance().getInitiativeService();
        userService = Apis.getInstance().getUserService();
    }

    public void loadData() {
        if (JointlyApplication.getConnection()) {
            fromAPI();
        } else {
            fromLocal();
        }
    }

    /**
     * Get the data from the API Service
     */
    private void fromAPI() {
        Call<APIResponse<Initiative>> initiativeCall = initiativeService.getListInitiative();
        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> list = response.body().getData();
                        if (list.isEmpty()) {
                            interactor.onNoData();
                        } else {
                            getUserOwners(list);
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Initiative>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        });
    }

    /**
     * Get the data from the local DB
     */
    private void fromLocal() {
        InitiativeRepository repository = InitiativeRepository.getInstance();
        String user = JointlyPreferences.getInstance().getUser();
        List<Initiative> list = repository.getList();
        List<User> userOwner = UserRepository.getInstance().getListInitiativeOwners();

        if (list.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(list, userOwner);
        }
    }

    /**
     * Get the list of users by the list of initiatives
     * @param initiatives
     */
    public void getUserOwners(List<Initiative> initiatives) {
        Call<APIResponse<User>> userCall = userService.getListUser();
        userCall.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<User> list = response.body().getData();
                        List<User> userList = list.stream().filter(x ->
                                    initiatives.stream().anyMatch(y ->
                                            y.getCreated_by().equals(x.getEmail())
                                    )
                                ).collect(Collectors.toList());

                        interactor.onSuccess(initiatives, userList);
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<User>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        });
    }

    public void syncData() {
        boolean isSuccessful = JointlyApplication.syncDataFromAPI();

        JointlyApplication.setConnection(isSuccessful);
    }
}
