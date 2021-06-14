package com.douglas.jointlyapp.ui.initiative;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.UserJoinInitiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class InitiativeInteractorImpl {

    interface ListInitiativeInteractor {
        void onNoDataCreatedHistory();
        void onNoDataCreatedInProgress();
        void onNoDataJoinedHistory();
        void onNoDataJoinedInProgress();
        void onSuccessCreatedInProgress(List<Initiative> list);
        void onSuccessJoinedInProgress(List<Initiative> list);
        void onSuccessCreatedHistory(List<Initiative> list);
        void onSuccessJoinedHistory(List<Initiative> list);
        void onSuccessParticipate();

        void onError(String message);
    }

    private final ListInitiativeInteractor interactor;
    private final SimpleDateFormat simpleDateFormat;
    private final UserService userService;

    public InitiativeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
        this.userService = Apis.getInstance().getUserService();
        this.simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
    }

    //region loadCreated

    /**
     * loadCreated
     */
    public void loadCreated() {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            createdFromAPI();
        } else {
            createdFromLocal();
        }
    }

    /**
     * createdFromAPI
     */
    private void createdFromAPI() {
        Call<APIResponse<List<Initiative>>> initiativeCall = userService.getListInitiativeCreated(JointlyPreferences.getInstance().getUser());

        initiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                Log.e("TAG", response.code() + "");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> history = filterHistory(response.body().getData());
                        List<Initiative> inProgress = filterInProgress(response.body().getData());
                        if (!history.isEmpty()) {
                            interactor.onSuccessCreatedHistory(history);
                        } else {
                            interactor.onNoDataCreatedHistory();
                        }
                        if (!inProgress.isEmpty()) {
                            interactor.onSuccessCreatedInProgress(inProgress);
                        } else {
                            interactor.onNoDataCreatedInProgress();
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
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

    //TODO Quizas obtener el usuario de firebase
    /**
     * createdFromLocal
     */
    private void createdFromLocal() {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();
        List<Initiative> list = InitiativeRepository.getInstance().getListCreatedByUser(user, false);
        List<Initiative> history = filterHistory(list);
        List<Initiative> inProgress = filterInProgress(list);
        if (!history.isEmpty()) {
            interactor.onSuccessCreatedHistory(history);
        } else {
            interactor.onNoDataCreatedHistory();
        }
        if (!inProgress.isEmpty()) {
            interactor.onSuccessCreatedInProgress(inProgress);
        } else {
            interactor.onNoDataCreatedInProgress();
        }
    }

    //endregion

    //region loadJoined

    /**
     * loadJoined
     */
    public void loadJoined() {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            joinedFromAPI();
        } else {
            joinedFromLocal();
        }
    }

    /**
     * joinedFromLocal
     */
    private void joinedFromLocal() {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();
        List<Initiative> list = InitiativeRepository.getInstance().getListJoinedByUser(user, 0, false);
        List<Initiative> history = filterHistory(list);
        List<Initiative> inProgress = filterInProgress(list);
        if (!history.isEmpty()) {
            interactor.onSuccessJoinedHistory(history);
        } else {
            interactor.onNoDataJoinedHistory();
        }
        if (!inProgress.isEmpty()) {
            interactor.onSuccessJoinedInProgress(inProgress);
        } else {
            interactor.onNoDataJoinedInProgress();
        }
    }

    /**
     * joinedFromAPI
     */
    private void joinedFromAPI() {
        Call<APIResponse<List<Initiative>>> initiativeCall = userService.getListInitiativeJoinedByUser(JointlyPreferences.getInstance().getUser(), 0);

        initiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                Log.e("TAG", response.code()+"");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> history = filterHistory(response.body().getData());
                        List<Initiative> inProgress = filterInProgress(response.body().getData());
                        if (!history.isEmpty()) {
                            interactor.onSuccessJoinedHistory(history);
                        } else {
                            interactor.onNoDataJoinedHistory();
                        }
                        if (!inProgress.isEmpty()) {
                            interactor.onSuccessJoinedInProgress(inProgress);
                        } else {
                            interactor.onNoDataJoinedInProgress();
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
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

    //region setParticipate

    //TODO comprobar con dos moviles y datos reales

    /**
     * setParticipate
     * @param ref_code
     */
    public void setParticipate(String ref_code) {
        String user = JointlyApplication.getCurrentSignInUser().getEmail();
        UserJoinInitiative joinToParticipate = InitiativeRepository.getInstance().getUserJoinToParticipate(ref_code, user, false);

        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()){
            setParticipateToAPI(joinToParticipate);
        } else {
            setParticipateToLocal(joinToParticipate);
        }
    }

    /**
     * setParticipateToLocal
     * @param userJoinInitiative
     */
    private void setParticipateToLocal(UserJoinInitiative userJoinInitiative) {
        InitiativeRepository.getInstance().updateUserJoin(userJoinInitiative);
        interactor.onSuccessParticipate();
    }

    /**
     * setParticipateToAPI
     * @param joinToParticipate
     */
    private void setParticipateToAPI(UserJoinInitiative joinToParticipate) {
        Call<APIResponse<UserJoinInitiative>> userJoinCall = Apis.getInstance().getInitiativeService().putUsersJoined(joinToParticipate.getId_initiative(), joinToParticipate.getUser_email(),1);
        userJoinCall.enqueue(new Callback<APIResponse<UserJoinInitiative>>() {
            @Override
            public void onResponse(Call<APIResponse<UserJoinInitiative>> call, Response<APIResponse<UserJoinInitiative>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Log.e("TAG", response.message());
                    if (!response.body().isError()) {
                        InitiativeRepository.getInstance().updateUserJoin(response.body().getData());
                        interactor.onSuccessParticipate();
                    } else {
                        interactor.onError(null);
                    }
                } else {
                    interactor.onError(null);
                }
            }

            @Override
            public void onFailure(Call<APIResponse<UserJoinInitiative>> call, Throwable t) {
                Log.e("TAG", t.getMessage());
                interactor.onError(null);
                call.cancel();
            }
        });
    }

    //endregion

    //region Filter Type

    /**
     * filterInProgress
     * @param list
     * @return List<Initiative>
     */
    private List<Initiative> filterInProgress(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        return simpleDateFormat.parse(CommonUtils.formatDateFromAPI(x.getTarget_date())).after(Calendar.getInstance(Locale.getDefault()).getTime());
                    } catch (ParseException e) {
                        Log.e("TAG", "------ERROR de filtrado in progress---------");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    /**
     * filterHistory
     * @param list
     * @return List<Initiative>
     */
    private List<Initiative> filterHistory(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        return simpleDateFormat.parse(CommonUtils.formatDateFromAPI(x.getTarget_date())).before(Calendar.getInstance(Locale.getDefault()).getTime());
                    } catch (ParseException e) {
                        Log.e("TAG", "------ERROR de filtrado history------");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    //endregion
}
