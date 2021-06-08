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
import com.douglas.jointlyapp.services.Apis;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowInitiativeInteractorImpl {

    interface ShowInitiativeInteractor {
        void onUserListEmpty();
        void onJoined();
        void onUnJoined();
        void onLoadUserStateJoined(boolean joined);
        void onLoadListUserJoined(List<User> userList);
        void onLoadUserOwner(User user);

        void onCannotDeleted();
        void onSuccessDeleted();
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

    //region loadUserStateJoined

    /**
     * Carga el estado del boton unirse
     * @param email
     */
    public void loadUserStateJoined(final String email, final long idInitiative) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadUserStateJoinedFromAPI(email, idInitiative);
        } else {
            loadUserStateJoinedFromLocal(email, idInitiative);
        }
    }

    /**
     *
     * @param email
     * @param idInitiative
     */
    private void loadUserStateJoinedFromLocal(String email, long idInitiative) {
        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(idInitiative, email, false);

        interactor.onLoadUserStateJoined(userJoinInitiative != null);
    }

    /**
     *
     * @param email
     * @param idInitiative
     */
    private void loadUserStateJoinedFromAPI(String email, long idInitiative) {
        Call<APIResponse<List<Initiative>>> userJoinInitiativeCall = userService.getListInitiativeJoinedByUser(email, 0);
        userJoinInitiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        Initiative i = response.body().getData().stream().filter(x -> x.getId() == idInitiative).findAny().orElse(null);
                        if (i != null) {
                            interactor.onLoadUserStateJoined(true);
                        } else {
                            interactor.onLoadUserStateJoined(false);
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
                call.cancel();
            }
        });
    }

    //endregion

    //region joinInitiative

    /**
     * El usuario se une a la iniciativa
     * @param initiative
     */
    public void joinInitiative(final Initiative initiative) {
        String user = JointlyPreferences.getInstance().getUser();
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            manageJoinToAPI(user, initiative);
        } else {
            manageJoinToLocal(user, initiative);
        }
    }

    /**
     *
     * @param user
     * @param initiative
     */
    private void manageJoinToLocal(String user, Initiative initiative) {
        UserJoinInitiative userJoinInitiative = InitiativeRepository.getInstance().getUserJoined(initiative.getId(), user, false);
        if(userJoinInitiative != null) {
            userJoinInitiative.setIs_deleted(true);
            userJoinInitiative.setIs_sync(false);
            InitiativeRepository.getInstance().updateUserJoin(userJoinInitiative);
            interactor.onUnJoined();
        } else {
            InitiativeRepository.getInstance().upsertUserJoin(new UserJoinInitiative(initiative.getId(), user, false, false));
            interactor.onJoined();
        }
    }

    /**
     *
     * @param user
     * @param initiatives
     */
    private void manageJoinToAPI(String user, Initiative initiatives) {
        Call<APIResponse<UserJoinInitiative>> insertCall = initiativeService.postUsersJoined(CommonUtils.getDateNow(), initiatives.getId(), user, 0);
        Call<APIResponse<UserJoinInitiative>> deleteCall = initiativeService.delUsersJoined(initiatives.getId(), user);

        insertCall.enqueue(new Callback<APIResponse<UserJoinInitiative>>() {
            @Override
            public void onResponse(Call<APIResponse<UserJoinInitiative>> call, Response<APIResponse<UserJoinInitiative>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        UserJoinInitiative userJoinInitiative = response.body().getData();
                        InitiativeRepository.getInstance().insertUserJoin(userJoinInitiative);
                        interactor.onJoined();
                    } else {
                        if(response.body().getData() != null)
                            deleteCall.enqueue(deleteUserJoin(initiatives.getId(), user));
                        else
                            interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserJoinInitiative>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    /**
     *
     * @param id
     * @param user
     * @return
     */
    private Callback<APIResponse<UserJoinInitiative>> deleteUserJoin(long id, String user) {
        return new Callback<APIResponse<UserJoinInitiative>>(){
            @Override
            public void onResponse(Call<APIResponse<UserJoinInitiative>> call, Response<APIResponse<UserJoinInitiative>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        InitiativeRepository.getInstance().deleteUserJoin(new UserJoinInitiative(id, user, true, true));
                        interactor.onUnJoined();
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserJoinInitiative>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        };
    }

    //endregion

    //region loadListUserJoined

    /**
     * Carga la lista de imagenes de los usuarios unidos a la iniciativa
     * @param idInitiative
     */
    public void loadListUserJoined(final long idInitiative) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadListUserJoinedFromAPI(idInitiative);
        } else {
            loadListUserJoinedFromLocal(idInitiative);
        }
    }

    /**
     *
     * @param idInitiative
     */
    private void loadListUserJoinedFromLocal(long idInitiative) {
        List<User> userList = UserRepository.getInstance().getListUserJoined(idInitiative, false);
        if(userList.isEmpty())
            interactor.onUserListEmpty();
        else
            interactor.onLoadListUserJoined(userList);
    }

    /**
     *
     * @param idInitiative
     */
    private void loadListUserJoinedFromAPI(long idInitiative) {
        Call<APIResponse<List<User>>> userCall = initiativeService.getListUsersJoinedByInitiative(idInitiative);
        userCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        if (response.body().getData().isEmpty()) {
                            interactor.onLoadListUserJoined(response.body().getData());
                        } else {
                            interactor.onUserListEmpty();
                        }
                    } else {
                        interactor.onError(null);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<User>>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    //endregion

    //region loadUserOwner

    /**
     * Carga la imagen del usuario creador de la iniciativa
     * @param email
     */
    public void loadUserOwner(final String email) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadUserOwnerFromAPI(email);
        } else {
            loadUserOwnerFromLocal(email);
        }
    }

    /**
     *
     * @param email
     */
    private void loadUserOwnerFromLocal(String email) {
        User user = UserRepository.getInstance().getUser(email);

        if(user != null) {
            interactor.onLoadUserOwner(user);
        }
    }

    /**
     *
     * @param email
     */
    private void loadUserOwnerFromAPI(String email) {
        Call<APIResponse<User>> userCall = userService.getUserByEmail(email);
        userCall.enqueue(new Callback<APIResponse<User>>() {
            @Override
            public void onResponse(Call<APIResponse<User>> call, Response<APIResponse<User>> response) {
                Log.e("TAG", response.message());
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        interactor.onLoadUserOwner(response.body().getData());
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

    //region deleteInitiative

    /**
     *
     * @param initiative
     */
    public void deleteInitiative(final Initiative initiative) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            deleteToAPI(initiative);
        } else {
            deleteToLocal(initiative);
        }
    }

    /**
     *
     * @param initiative
     */
    private void deleteToLocal(final Initiative initiative) {
        List<User> userList = UserRepository.getInstance().getListUserJoined(initiative.getId(), false);

        if(!userList.isEmpty()) {
            interactor.onCannotDeleted();
            return;
        }

        Initiative iniDelete = InitiativeRepository.getInstance().getInitiative(initiative.getId(), false);

        if(iniDelete != null) {
            iniDelete.setIs_deleted(true);
            iniDelete.setIs_sync(false);
            InitiativeRepository.getInstance().update(iniDelete);
            interactor.onSuccessDeleted();
        }
    }

    /**
     *
     * @param initiative
     */
    private void deleteToAPI(final Initiative initiative) {
        Call<APIResponse<Initiative>> deleteInitiativeCall = initiativeService.delInitiative(initiative.getId());
        Call<APIResponse<List<User>>> usersJoinedCall = initiativeService.getListUsersJoinedByInitiative(initiative.getId());

        usersJoinedCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        if(!response.body().getData().isEmpty()) {
                            interactor.onCannotDeleted();
                        } else {
                            deleteInitiativeCall.enqueue(deleteInitiativeCallback(initiative));
                        }
                    } else {
                        interactor.onError(null);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<User>>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    /**
     *
     * @param initiative
     * @return
     */
    private Callback<APIResponse<Initiative>> deleteInitiativeCallback(Initiative initiative) {
        return new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        InitiativeRepository.getInstance().delete(initiative);
                        interactor.onSuccessDeleted();
                    } else {
                        interactor.onCannotDeleted();
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<Initiative>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        };
    }

    //endregion
}
