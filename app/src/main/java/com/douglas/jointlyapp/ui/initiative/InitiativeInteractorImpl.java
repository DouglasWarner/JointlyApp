package com.douglas.jointlyapp.ui.initiative;

import android.util.Log;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.services.APIResponse;
import com.douglas.jointlyapp.services.Apis;
import com.douglas.jointlyapp.services.UserService;
import com.douglas.jointlyapp.ui.JointlyApplication;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        this.simpleDateFormat = new SimpleDateFormat(JointlyApplication.FORMAT_DD_MM_YYYY_HH_MM, Locale.getDefault());
    }

    //region loadCreated

    /**
     *
     * @param history
     */
    public void loadCreated(final String history) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            createdFromAPI(history);
        } else {
            createdFromLocal(history);
        }
    }

    /**
     *
     * @param history
     */
    private void createdFromAPI(final String history) {
        Call<APIResponse<List<Initiative>>> initiativeCall = userService.getListInitiativeCreated(JointlyPreferences.getInstance().getUser());

        initiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                Log.e("TAG", response.code()+"");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> list = response.body().getData();
                        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
                            if(list.isEmpty()) {
                                interactor.onNoDataCreatedHistory();
                            } else {
                                interactor.onSuccessCreatedHistory(filterHistory(list));
                            }
                        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
                            if(list.isEmpty()) {
                                interactor.onNoDataCreatedInProgress();
                            } else {
                                interactor.onSuccessCreatedInProgress(filterInProgress(list));
                            }
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
     *
     * @param history
     */
    private void createdFromLocal(final String history) {
        String user = JointlyPreferences.getInstance().getUser();
        List<Initiative> list = InitiativeRepository.getInstance().getListCreatedByUser(user, false);
        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
            if (list.isEmpty()) {
                interactor.onNoDataCreatedHistory();
            } else {
                interactor.onSuccessCreatedHistory(filterHistory(list));
            }
        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
            if (list.isEmpty()) {
                interactor.onNoDataCreatedInProgress();
            } else {
                interactor.onSuccessCreatedInProgress(filterInProgress(list));
            }
        }
    }

    //endregion

    //region loadJoined

    /**
     *
     * @param history
     * @param type
     */
    public void loadJoined(final String history, final int type) {
        if(JointlyApplication.getConnection() && JointlyApplication.isIsSyncronized()) {
            joinedFromAPI(history, type);
        } else {
            joinedFromLocal(history, type);
        }
    }

    /**
     *
     * @param history
     */
    private void joinedFromLocal(final String history, final int type) {
        String user = JointlyPreferences.getInstance().getUser();
        List<Initiative> list = InitiativeRepository.getInstance().getListJoinedByUser(user, type, false);
        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
            if (list.isEmpty()) {
                interactor.onNoDataJoinedHistory();
            } else {
                interactor.onSuccessJoinedHistory(filterHistory(list));
            }
        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
            if (list.isEmpty()) {
                interactor.onNoDataJoinedInProgress();
            } else {
                interactor.onSuccessJoinedInProgress(filterInProgress(list));
            }
        }
    }

    /**
     *
     * @param history
     * @param type
     */
    private void joinedFromAPI(final String history, final int type) {
        Call<APIResponse<List<Initiative>>> initiativeCall = userService.getListInitiativeJoinedByUser(JointlyPreferences.getInstance().getUser(), type);

        initiativeCall.enqueue(new Callback<APIResponse<List<Initiative>>>() {
            @Override
            public void onResponse(Call<APIResponse<List<Initiative>>> call, Response<APIResponse<List<Initiative>>> response) {
                Log.e("TAG", response.code()+"");
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().isError()) {
                        List<Initiative> list = response.body().getData();
                        if(history.equals(InitiativeFragment.TYPE_HISTORY)) {
                            if(list.isEmpty()) {
                                interactor.onNoDataJoinedHistory();
                            } else {
                                interactor.onSuccessJoinedHistory(filterHistory(list));
                            }
                        } else if(history.equals(InitiativeFragment.TYPE_INPROGRESS)) {
                            if(list.isEmpty()) {
                                interactor.onNoDataJoinedInProgress();
                            } else {
                                interactor.onSuccessJoinedInProgress(filterInProgress(list));
                            }
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

    //region Filter Type

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
                        Log.e("TAG", simpleDateFormat.parse(x.getTarget_date()) + "-------" + GregorianCalendar.getInstance(Locale.getDefault()).getTime() + "");
                        return simpleDateFormat.parse(x.getTarget_date()).after(GregorianCalendar.getInstance(Locale.getDefault()).getTime());
                    } catch (ParseException e) {
                        Log.e("TAG", "------ERROR de filtrado in progress---------");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    /**
     *
     * @param list
     * @return
     */
    private List<Initiative> filterHistory(List<Initiative> list) {
        return list.stream()
                .filter(x -> {
                    try {
                        Log.e("TAG", simpleDateFormat.parse(x.getTarget_date())+ "---------" + GregorianCalendar.getInstance(Locale.getDefault()).getTime() + "");
                        return simpleDateFormat.parse(x.getTarget_date()).before(GregorianCalendar.getInstance(Locale.getDefault()).getTime());
                    } catch (ParseException e) {
                        Log.e("TAG", "------ERROR de filtrado history------");
                        return false;
                    }
                }).collect(Collectors.toList());
    }

    //endregion
}
