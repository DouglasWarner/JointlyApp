package com.douglas.jointlyapp.ui.showinitiative;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowInitiativeInteractorImpl {

    interface ShowInitiativeInteractor
    {
        void onUserListEmpty();
        void onJoined();
        void onUnJoined();
        void onLoadListUserJoined(List<User> userList);
        void onLoadUserOwner(User user);
        void onSuccessLoad(Initiative initiative);
        void onError(String message);
    }

    private ShowInitiativeInteractor interactor;
    private InitiativeService initiativeService;
    private UserService userService;

    public ShowInitiativeInteractorImpl(ShowInitiativeInteractor interactor) {
        this.interactor = interactor;
        this.initiativeService = Apis.getInstance().getInitiativeService();
        this.userService = Apis.getInstance().getUserService();
    }

    /**
     * Carga el estado del boton unirse
     * @param email
     */
    public void loadUserStateJoined(final String email, final int idInitiative)
    {
        if(JointlyApplication.getConnection()) {
            loadUserStateJoinedFromAPI(email, idInitiative);
        } else {
            loadUserStateJoinedFromLocal(email, idInitiative);
        }
    }

    /**
     * El usuario se une a la iniciativa
     * @param initiative
     */
    public void joinInitiative(final Initiative initiative) {

        String user = JointlyPreferences.getInstance().getUser();

        UserJoinInitiative userJoinInitiative = new UserJoinInitiative(initiative.getId(), user, CommonUtils.getDateNow(), 0);

        InitiativeRepository.getInstance().insertUserJoin(userJoinInitiative);

        interactor.onJoined();
    }

    /**
     * El usuario cancela la union a la iniciativa
     * @param initiative
     */
    public void unJoinInitiative(final Initiative initiative)
    {
        String user = JointlyPreferences.getInstance().getUser();

        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(initiative.getId(), user);

        InitiativeRepository.getInstance().deleteUserJoin(userJoinInitiative);

        interactor.onUnJoined();
    }

    /**
     * Carga la lista de imagenes de los usuarios unidos a la iniciativa
     * @param idInitiative
     */
    public void loadListUserJoined(final long idInitiative)
    {
        if(JointlyApplication.getConnection()) {
            loadListUserJoinedFromAPI(idInitiative);
        } else {
            loadListUserJoinedFromLocal(idInitiative);
        }
    }

    /**
     * Carga la imagen del usuario creador de la iniciativa
     * @param email
     */
    public void loadUserOwner(final String email)
    {
        if(JointlyApplication.getConnection()) {
            loadUserOwnerFromAPI(email);
        } else {
            loadUserOwnerFromLocal(email);
        }
    }

    //region FROM API

    private void loadUserStateJoinedFromAPI(String email, int idInitiative) {
        Call<APIResponse<Initiative>> userJoinInitiativeCall = userService.getListInitiativeJoinedByUser(email, 0);
        userJoinInitiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        Initiative i = response.body().getData().stream().filter(x -> x.getId() == idInitiative).findAny().orElse(null);
                        if (i != null) {
                            interactor.onJoined();
                        } else {
                            interactor.onUnJoined();
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
                call.cancel();
            }
        });
    }

    private void loadListUserJoinedFromAPI(long idInitiative) {
        Call<APIResponse<User>> userCall = initiativeService.getListUsersJoinedByInitiative(idInitiative);
        userCall.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        if (response.body().getData().isEmpty()) {
                            interactor.onLoadListUserJoined(response.body().getData());
                        } else {
                            interactor.onUserListEmpty();
                        }
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
                call.cancel();
            }
        });
    }

    private void loadUserOwnerFromAPI(String email) {
        Call<APIResponse<User>> userCall = userService.getUserByEmail(email);
        userCall.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        interactor.onLoadUserOwner(response.body().getData().get(0));
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
                call.cancel();
            }
        });
    }

    //endregion

    //region FROM LOCAL

    private void loadUserStateJoinedFromLocal(String email, int idInitiative) {
        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(idInitiative, email);

        if(userJoinInitiative == null)
            interactor.onUnJoined();
        else
            interactor.onJoined();
    }

    private void loadListUserJoinedFromLocal(long idInitiative) {
        List<User> userList = UserRepository.getInstance().getListUserJoined(idInitiative);

        if(userList.isEmpty())
            interactor.onUserListEmpty();
        else
            interactor.onLoadListUserJoined(userList);
    }

    private void loadUserOwnerFromLocal(String email) {
        User user = UserRepository.getInstance().getUser(email);

        interactor.onLoadUserOwner(user);
    }

    //endregion
}
