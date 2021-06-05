package com.douglas.jointlyapp.ui.initiative;

import android.util.Log;
import android.widget.Toast;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.InitiativeService;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;
import com.douglas.jointlyapp.ui.utils.service.Apis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitiativeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoDataCreatedInProgress();
        void onNoDataCreatedHistory();
        void onNoDataJoinedInProgress();
        void onNoDataJoinedHistory();
        void onSuccessCreatedInProgress(List<Initiative> list);
        void onSuccessJoinedInProgress(List<Initiative> list);
        void onSuccessCreatedHistory(List<Initiative> list);
        void onSuccessJoinedHistory(List<Initiative> list);

        void onError(String message);
    }

    private final ListInitiativeInteractor interactor;
    private final SimpleDateFormat simpleDateFormat;
    private final UserService userService;

    public InitiativeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
        this.userService = Apis.getInstance().getUserService();
        this.simpleDateFormat = new SimpleDateFormat(JointlyApplication.DATETIMEFORMAT, Locale.getDefault());
    }

    public void loadCreated(final String history) {
        if(JointlyApplication.getConnection()) {
            createdFromAPI(history);
        } else {
            createdFromLocal(history);
        }
    }

    public void loadJoined(final String history, final int type) {
        if(JointlyApplication.getConnection()) {
            joinedFromAPI(history, type);
        } else {
            joinedFromLocal(history);
        }
    }

    private void createdFromAPI(final String history) {
        Call<APIResponse<Initiative>> initiativeCall = userService.getListInitiativeCreated(JointlyPreferences.getInstance().getUser());

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e("TAG", response.code()+"");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> list = response.body().getData();
                        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
                            list = filterHistory(list);
                            if(list.isEmpty()) {
                                interactor.onNoDataCreatedHistory();
                            } else {
                                interactor.onSuccessCreatedHistory(list);
                            }
                        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
                            list = filterInProgress(list);
                            if(list.isEmpty()) {
                                interactor.onNoDataCreatedInProgress();
                            } else {
                                interactor.onSuccessCreatedInProgress(list);
                            }
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
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

    private void joinedFromAPI(final String history, final int type) {
        Call<APIResponse<Initiative>> initiativeCall = userService.getListInitiativeJoinedByUser(JointlyPreferences.getInstance().getUser(), type);

        initiativeCall.enqueue(new Callback<APIResponse<Initiative>>() {
            @Override
            public void onResponse(Call<APIResponse<Initiative>> call, Response<APIResponse<Initiative>> response) {
                Log.e("TAG", response.code()+"");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> list = response.body().getData();
                        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
                            list = filterHistory(list);
                            if(list.isEmpty()) {
                                interactor.onNoDataJoinedHistory();
                            } else {
                                interactor.onSuccessJoinedHistory(list);
                            }
                        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
                            list = filterInProgress(list);
                            if(list.isEmpty()) {
                                interactor.onNoDataJoinedInProgress();
                            } else {
                                interactor.onSuccessJoinedInProgress(list);
                            }
                        }
                    } else {
                        interactor.onError(response.body().getMessage());
                    }
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

    private void createdFromLocal(final String history) {
        String user = JointlyPreferences.getInstance().getUser();
        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
            List<Initiative> list = filterHistory(InitiativeRepository.getInstance().getListCreatedByUser(user));
            if (list.isEmpty()) {
                interactor.onNoDataCreatedHistory();
            } else {
                interactor.onSuccessCreatedHistory(list);
            }
        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
            List<Initiative> list = filterInProgress(InitiativeRepository.getInstance().getListCreatedByUser(user));
            if (list.isEmpty()) {
                interactor.onNoDataCreatedInProgress();
            } else {
                interactor.onSuccessCreatedInProgress(list);
            }
        }
    }

    private void joinedFromLocal(final String history) {
        String user = JointlyPreferences.getInstance().getUser();
        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
            List<Initiative> list = filterHistory(InitiativeRepository.getInstance().getListJoinedByUser(user));
            if (list.isEmpty()) {
                interactor.onNoDataJoinedHistory();
            } else {
                interactor.onSuccessJoinedHistory(list);
            }
        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
            List<Initiative> list = filterInProgress(InitiativeRepository.getInstance().getListJoinedByUser(user));
            if (list.isEmpty()) {
                interactor.onNoDataJoinedInProgress();
            } else {
                interactor.onSuccessJoinedInProgress(list);
            }
        }
    }

    private List<Initiative> filterInProgress(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        return simpleDateFormat.parse(x.getTarget_date()).after(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                    } catch (ParseException e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    private List<Initiative> filterHistory(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        return simpleDateFormat.parse(x.getTarget_date()).before(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                    } catch (ParseException e) {
                        return false;
                    }
                }).collect(Collectors.toList());
    }
}
