package com.douglas.jointlyapp.ui.infouser;

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

/**
 * Entity who connect with the APIS and LOCALDB
 */
public class InfoUserInteractorImpl {

    interface ProfileInteractor {
        void onInitiativeCreatedEmpty();
        void onInitiativeJointedEmpty();
        void onCountUserFollow(long count);
        void onCountUserParticipate(long count);
        void onSuccessListCreated(List<Initiative> listInitiativesCreated);
        void onSuccessListJoined(List<Initiative> listInitiativesJoined);

        void onError(String message);
    }

    private ProfileInteractor interactor;
    private static InitiativeService initiativeService;
    private static UserService userService;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());

    public InfoUserInteractorImpl(ProfileInteractor interactor) {
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
        List<Initiative> listCreatedByUser;
        List<Initiative> listJoinedByUser;
        listCreatedByUser = InitiativeRepository.getInstance().getListCreatedByUser(user.getEmail(), false);
        listJoinedByUser = InitiativeRepository.getInstance().getListJoinedByUser(user.getEmail(), 0, false);

        if(listCreatedByUser.isEmpty()) {
            interactor.onInitiativeCreatedEmpty();
        } else {
            interactor.onSuccessListCreated(filterInProgress(listCreatedByUser));
        }
        if(listJoinedByUser.isEmpty()) {
            interactor.onInitiativeJointedEmpty();
        } else {
            interactor.onSuccessListJoined(filterInProgress(listJoinedByUser));
        }
    }

    /**
     *
     * @param user
     */
    private void loadListInitiativeFromAPI(User user) {
        Call<APIResponse<List<Initiative>>> initiativeCreatedCall = userService.getListInitiativeCreated(user.getEmail());
        Call<APIResponse<List<Initiative>>> initiativeJoinedCall = userService.getListInitiativeJoinedByUser(user.getEmail(), 0);

        initiativeCreatedCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(!response.body().isError()) {
                        List<Initiative> listCreatedByUser = new ArrayList<>(response.body().getData());
                        if(listCreatedByUser.isEmpty()) {
                            interactor.onInitiativeCreatedEmpty();
                        } else {
                            interactor.onSuccessListCreated(filterInProgress(listCreatedByUser));
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
                        List<Initiative> listJoinedByUser = new ArrayList<>(response.body().getData());
                        if(listJoinedByUser.isEmpty()) {
                            interactor.onInitiativeJointedEmpty();
                        } else {
                            interactor.onSuccessListJoined(filterInProgress(listJoinedByUser));
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
