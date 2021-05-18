package com.douglas.jointlyapp.ui.initiative;

import android.os.Handler;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.ui.preferences.JointlyPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class InitiativeInteractorImpl {

    interface ListInitiativeInteractor
    {
        void onNoData();
        void onSuccessCreatedInProgress(List<Initiative> list);
        void onSuccessJoinedInProgress(List<Initiative> list);
        void onSuccessCreatedHistory(List<Initiative> list);
        void onSuccessJoinedInHistory(List<Initiative> list);
    }

    private ListInitiativeInteractor interactor;
    private SimpleDateFormat simpleDateFormat;

    public InitiativeInteractorImpl(ListInitiativeInteractor interactor) {
        this.interactor = interactor;
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    public void loadInitiativeCreatedInProgress()
    {
        new Handler().postDelayed(() -> {

            String user = JointlyPreferences.getInstance().getUser();
            List<Initiative> list = InitiativeRepository.getInstance().getListCreatedInProgressByUser(user)
                    .stream()
                    .filter(x -> {
                        try {
                            return simpleDateFormat.parse(x.getTargetDate()).after(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                        } catch (ParseException e) {
                            return false;
                        }
                    }).collect(Collectors.toList());

            if (list.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccessCreatedInProgress(list);
            }

        }, 500);
    }

    public void loadInitiativeJoinedInProgress()
    {
        new Handler().postDelayed(() -> {
            String user = JointlyPreferences.getInstance().getUser();
            List<Initiative> list = InitiativeRepository.getInstance().getListJoinedInProgressByUser(user)
                    .stream()
                    .filter(x -> {
                        try {
                            return simpleDateFormat.parse(x.getTargetDate()).after(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                        } catch (ParseException e) {
                            return false;
                        }
                    }).collect(Collectors.toList());

            if (list.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccessJoinedInProgress(list);
            }
        },500);
    }

    public void loadInitiativeCreatedHistory()
    {
        new Handler().postDelayed(() -> {
            String user = JointlyPreferences.getInstance().getUser();
            List<Initiative> list = InitiativeRepository.getInstance().getListCreatedHistoryByUser(user)
                    .stream()
                    .filter(x -> {
                        try {
                            return simpleDateFormat.parse(x.getTargetDate()).before(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                        } catch (ParseException e) {
                            return false;
                        }
                    }).collect(Collectors.toList());

            if (list.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccessCreatedHistory(list);
            }
        },500);
    }

    public void loadInitiativeJoinedHistory()
    {
        new Handler().postDelayed(() -> {
            String user = JointlyPreferences.getInstance().getUser();
            List<Initiative> list = InitiativeRepository.getInstance().getListJoinedHistoryByUser(user)
                    .stream()
                    .filter(x -> {
                        try {
                            return simpleDateFormat.parse(x.getTargetDate()).before(Calendar.getInstance(TimeZone.getTimeZone("UTF")).getTime());
                        } catch (ParseException e) {
                            return false;
                        }
                    }).collect(Collectors.toList());

            if (list.isEmpty()) {
                interactor.onNoData();
            } else {
                interactor.onSuccessJoinedInHistory(list);
            }
        },500);
    }
}
