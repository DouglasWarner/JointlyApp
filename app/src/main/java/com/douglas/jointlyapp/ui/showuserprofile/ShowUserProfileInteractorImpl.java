package com.douglas.jointlyapp.ui.showuserprofile;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.model.UserFollowUser;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowUserProfileInteractorImpl {

    interface ProfileInteractor {
        void onInitiativeCreatedEmpty();
        void onInitiativeJointedEmpty();
        void onSuccessUnFollow();
        void onSuccessFollow();
        void onUserStateFollow(boolean follow);
        void onCountUserFollow(long count);
        void onCountUserParticipate(long count);
        void onSuccess(List<Initiative> listInitiativesCreated, List<Initiative> listInitiativesJoined);

        void onError(String message);
    }

    private ProfileInteractor interactor;
    private static InitiativeService initiativeService;
    private static UserService userService;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.DATETIMEFORMAT, Locale.getDefault());

    public ShowUserProfileInteractorImpl(ProfileInteractor interactor) {
        this.interactor = interactor;
        initiativeService = Apis.getInstance().getInitiativeService();
        userService = Apis.getInstance().getUserService();
    }

    //region loadListInitiative

    /**
     *
     * @param user
     */
    public void loadListInitiative(User user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadListInitiativeFromAPI(user);
        } else {
            loadListInitiativeFromLocal(user);
        }
    }

    /**
     *
     * @param user
     */
    private void loadListInitiativeFromLocal(User user) {
        List<Initiative> listCreatedByUser = InitiativeRepository.getInstance().getListCreatedByUser(user.getEmail(), false);
        List<Initiative> listJoinedByUser = InitiativeRepository.getInstance().getListJoinedByUser(user.getEmail(), 0, false);

        if(listCreatedByUser.isEmpty()) {
            interactor.onInitiativeCreatedEmpty();
            return;
        }
        if(listJoinedByUser.isEmpty()) {
            interactor.onInitiativeJointedEmpty();
            return;
        }

        interactor.onSuccess(filterInProgress(listCreatedByUser), filterInProgress(listJoinedByUser));
    }

    /**
     *
     * @param user
     */
    private void loadListInitiativeFromAPI(User user) {
        Call<APIResponse<List<Initiative>>> initiativeCreatedCall = userService.getListInitiativeCreated(user.getEmail());
        Call<APIResponse<List<Initiative>>> initiativeJoinedCall = userService.getListInitiativeJoinedByUser(user.getEmail(), 0);
        List<Initiative> listCreated = new ArrayList<>();
        List<Initiative> listJoined = new ArrayList<>();

        initiativeCreatedCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        listCreated.addAll(response.body().getData());
                        if(listCreated.isEmpty()) {
                            interactor.onInitiativeCreatedEmpty();
                        } else {
                            interactor.onSuccess(filterInProgress(listCreated), new ArrayList<>());
                        }
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<Initiative>>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                initiativeCreatedCall.cancel();
            }
        });

        initiativeJoinedCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        listJoined.addAll(response.body().getData());
                        if(listJoined.isEmpty()) {
                            interactor.onInitiativeJointedEmpty();
                        } else {
                            interactor.onSuccess(listCreated, listJoined);
                        }
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<Initiative>>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                initiativeJoinedCall.cancel();
            }
        });
    }

    //endregion

    //region loadCountUserFollow

    /**
     *
     * @param user
     */
    public void loadCountUserFollow(User user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadCountUserFollowFromAPI(user);
        } else {
            loadCountUserFollowFromLocal(user);
        }
    }

    /**
     *
     * @param user
     */
    private void loadCountUserFollowFromLocal(User user) {
        long count = UserRepository.getInstance().getCountUserFollowers(user.getEmail());

        interactor.onCountUserFollow(count);
    }

    /**
     *
     * @param user
     */
    private void loadCountUserFollowFromAPI(User user) {
        Call<APIResponse<List<UserFollowUser>>> countUserFollowCall = userService.getListUserFollow();
        countUserFollowCall.enqueue(new Callback<APIResponse<List<UserFollowUser>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<UserFollowUser>>> call, Response<APIResponse<List<UserFollowUser>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        long count = response.body().getData().stream().filter(x -> x.getUser_follow().equals(user.getEmail())).count();
                        interactor.onCountUserFollow(count);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<UserFollowUser>>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                countUserFollowCall.cancel();
            }
        });
    }

    //endregion

    //region loadUserStateFollow

    /**
     *
     * @param user
     */
    public void loadUserStateFollow(User user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadUserStateFollowFromAPI(user);
        } else {
            loadUserStateFollowFromLocal(user);
        }
    }

    /**
     *
     * @param userFollow
     */
    private void loadUserStateFollowFromLocal(User userFollow) {
        //TODO Quizas obtener de firebase
        String user = JointlyPreferences.getInstance().getUser();
        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user, userFollow.getEmail(), false);

        interactor.onUserStateFollow(userFollowUser != null);
    }

    /**
     *
     * @param userFollow
     */
    private void loadUserStateFollowFromAPI(User userFollow) {
        String user = JointlyPreferences.getInstance().getUser();

        Call<APIResponse<List<User>>> userFollowCall = userService.getUserFollowed(user);
        userFollowCall.enqueue(new Callback<APIResponse<List<User>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<User>>> call, Response<APIResponse<List<User>>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        interactor.onUserStateFollow(response.body().getData().stream().anyMatch(x-> x.getEmail().equals(userFollow.getEmail())));
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
                userFollowCall.cancel();
            }
        });
    }

    //endregion

    //region manageFollowUser

    /**
     *
     * @param userFollow
     */
    public void manageFollowUser(User userFollow) {
        String user = JointlyPreferences.getInstance().getUser();
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            manageFollowUserFromAPI(user, userFollow);
        } else {
            manageFollowUserFromLocal(user, userFollow);
        }
    }

    /**
     *
     * @param userFollow
     */
    private void manageFollowUserFromLocal(String user, User userFollow) {
        UserFollowUser userFollowUser = UserRepository.getInstance().getUserFollowUser(user, userFollow.getEmail(), false);

        if(userFollowUser != null) {
            UserRepository.getInstance().updateUserFollowed(new UserFollowUser(user, userFollow.getEmail(), true, false));
            interactor.onSuccessUnFollow();
        } else {
            UserRepository.getInstance().upsertUserFollowUser(new UserFollowUser(user, userFollow.getEmail(), false, false));
            interactor.onSuccessFollow();
        }
    }

    /**
     *
     * @param user
     * @param userFollow
     */
    private void manageFollowUserFromAPI(String user, User userFollow) {
        Call<APIResponse<UserFollowUser>> insertUserFollowCall = userService.postUserFollow(user, userFollow.getEmail());
        Call<APIResponse<UserFollowUser>> deleteUserFollowCall = userService.delUserFollow(user, userFollow.getEmail());

        insertUserFollowCall.enqueue(new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        Log.e("TAG-------------> ", String.valueOf(response.body().getData()));
                        UserFollowUser followUser = response.body().getData();
                        if (followUser != null) {
                            UserRepository.getInstance().insertUserFollowed(followUser);
                            interactor.onSuccessFollow();
                        }
                    } else {
                        deleteUserFollowCall.enqueue(deleteUserFollowCallBack(user, userFollow.getEmail()));
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        });
    }

    private Callback<APIResponse<UserFollowUser>> deleteUserFollowCallBack(String user, String userFollow) {
        return new Callback<APIResponse<UserFollowUser>>() {
            @Override
            public void onResponse(Call<APIResponse<UserFollowUser>> call, Response<APIResponse<UserFollowUser>> response) {
                Log.e("TAG", response.message());
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        UserRepository.getInstance().deleteUserFollowed(new UserFollowUser(user, userFollow, true, true));
                        interactor.onSuccessUnFollow();
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserFollowUser>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
                interactor.onError(null);
            }
        };
    }

    //endregion

    //region loadCountUserParticipate

    /**
     *
     * @param user
     */
    public void loadCountUserParticipate(User user) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            loadCountUserParticipateFromAPI(user);
        } else {
            loadCountUserParticipateFromLocal(user);
        }
    }

    /**
     *
     * @param user
     */
    private void loadCountUserParticipateFromLocal(User user) {
        long countInitiativeParticipate = InitiativeRepository.getInstance().getCountInitiativeParticipateByUser(user.getEmail(), 1, false);

        interactor.onCountUserParticipate(countInitiativeParticipate);
    }

    /**
     *
     * @param user
     */
    private void loadCountUserParticipateFromAPI(User user) {
        Call<APIResponse<List<UserJoinInitiative>>> countParticipateByUserCall = initiativeService.getListUserJoined();
        countParticipateByUserCall.enqueue(new Callback<APIResponse<List<UserJoinInitiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<UserJoinInitiative>>> call, Response<APIResponse<List<UserJoinInitiative>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        long count = response.body().getData().stream().filter(x -> x.getUser_email().equals(user.getEmail()) && x.getType()==1).count();
                        interactor.onCountUserParticipate(count);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<List<UserJoinInitiative>>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                countParticipateByUserCall.cancel();
            }
        });
    }

    //endregion

    //TODO checkear si esta haciendo bien el filtrado

    /**
     *
     * @param list
     * @return
     */
    private List<Initiative> filterInProgress(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        return simpleDateFormat.parse(x.getTarget_date()).after(GregorianCalendar.getInstance(Locale.getDefault()).getTime());
                    } catch (ParseException e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }
}
