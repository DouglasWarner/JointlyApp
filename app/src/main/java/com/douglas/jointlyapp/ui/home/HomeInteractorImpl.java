package com.douglas.jointlyapp.ui.home;

import android.util.Log;

import com.douglas.jointlyapp.data.model.HomeListAdapter;
import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractorImpl {

    interface ListInitiativeInteractor {
        void onNoData();
        void onSuccess(List<HomeListAdapter> homeListAdapters);
        void onError(String message);
        void onSync();
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

    //region loadData

    /**
     *
     */
    public void loadData() {
        if (JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            fromAPI();
        } else {
            fromLocal();
        }
    }

    /**
     * Get the data from the API Service
     */
    private void fromAPI() {
        Call<APIResponse<List<Initiative>>> initiativeCall = initiativeService.getListInitiative();
        initiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
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
            public void onFailure(Call<APIResponse<List<Initiative>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                initiativeCall.cancel();
            }
        });
    }

    /**
     * Get the data from the local DB
     */
    private void fromLocal() {
        //TODO Quizas obtener de firebase
        String user = JointlyPreferences.getInstance().getUser();

        List<Initiative> listInitiative = InitiativeRepository.getInstance().getList(false);
        List<User> userOwner = UserRepository.getInstance().getListInitiativeOwners(false);
        List<UserJoinInitiative> joinInitiatives = InitiativeRepository.getInstance().getListUserJoinInitiative(false);
        List<Long> listCountUsersJoined = getCountUserJoineds(listInitiative, joinInitiatives);

        List<HomeListAdapter> homeListAdapters = getHomeListAdapters(listInitiative, userOwner, listCountUsersJoined);

        if (listInitiative.isEmpty()) {
            interactor.onNoData();
        } else {
            interactor.onSuccess(homeListAdapters);
        }
    }

    /**
     *
     * @param listInitiative
     * @param userOwner
     * @param listCountUsersJoined
     * @return
     */
    @NotNull
    private List<HomeListAdapter> getHomeListAdapters(List<Initiative> listInitiative, List<User> userOwner, List<Long> listCountUsersJoined) {
        List<HomeListAdapter> homeListAdapters = new ArrayList<>();

        for (int i = 0; i < listInitiative.size(); i++) {
            int finalI = i;
            homeListAdapters.add(
                    new HomeListAdapter(listInitiative.get(i),
                            userOwner.stream().findAny().filter(x -> x.getEmail().equals(listInitiative.get(finalI).getCreated_by())).orElse(null),
                            listCountUsersJoined.get(i))
            );
        }
        return homeListAdapters;
    }

    /**
     *
     * @param listInitiative
     * @param joinInitiatives
     * @return
     */
    @NotNull
    private List<Long> getCountUserJoineds(List<Initiative> listInitiative, List<UserJoinInitiative> joinInitiatives) {
        List<Long> listCountUsersJoined = new ArrayList<>();

        listInitiative.forEach(i -> listCountUsersJoined.add(joinInitiatives.stream().filter(x -> !x.getIs_deleted() && x.getId_initiative()==i.getId()).count()));
        return listCountUsersJoined;
    }

    //endregion

    //region getUserOwners and getCountUsersJoined

    /**
     * Get the list of users by the list of initiatives
     * @param initiatives
     */
    public void getUserOwners(List<Initiative> initiatives) {
        Call<APIResponse<List<User>>> userCall = userService.getListUser();
        userCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        List<User> list = response.body().getData();
                        List<User> userList = list.stream().filter(x ->
                                    initiatives.stream().anyMatch(y ->
                                            y.getCreated_by().equals(x.getEmail())
                                    )
                                ).collect(Collectors.toList());
                        getCountUsersJoined(initiatives, userList);
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<User>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                userCall.cancel();
            }
        });
    }

    private void getCountUsersJoined(List<Initiative> initiatives, List<User> listUserOwner) {
        Call<APIResponse<List<UserJoinInitiative>>> userCall = initiativeService.getListUserJoined();
        userCall.enqueue(new Callback<APIResponse<List<UserJoinInitiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<UserJoinInitiative>>> call, Response<APIResponse<List<UserJoinInitiative>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        List<UserJoinInitiative> userJoinInitiatives = response.body().getData();
                        List<Long> listCountUsersJoined = getCountUserJoineds(initiatives, userJoinInitiatives);
                        List<HomeListAdapter> homeListAdapters = getHomeListAdapters(initiatives, listUserOwner, listCountUsersJoined);
                        interactor.onSuccess(homeListAdapters);
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<UserJoinInitiative>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                userCall.cancel();
            }
        });
    }

    //endregion

    public void syncData() {

        InitiativeRepository.getInstance().tmpInsert();
//        SyncToAPI syncToAPI = new SyncToAPI(() -> null);
//        syncToAPI.run();
//
//        SyncFromAPI syncFromAPI = new SyncFromAPI(() -> null);
//        syncFromAPI.run();

        interactor.onSync();
    }
}
