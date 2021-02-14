package com.douglas.jointlyapp.ui.initiative.manage;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.text.TextUtils;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ManageInitiativeInteractorImpl {

    interface ManageInitiativeInteractor
    {
        void onNameEmpty();
        void onNameFormatError();
        void onLocationEmpty();
        void onTargetAreaEmpty();
        void onTargetDateEmpty();
        void onTargetDateBeforeNowError();
        void onTargetTimeEmpty();
        void onTargetAmountEmpty();
        void onTargetAmountFormatError();

        void onCannotDeleted();
        void onSuccessDeleted();

        void onSuccess();
    }

    private ManageInitiativeInteractor manageInitiativeInteractor;

    public ManageInitiativeInteractorImpl(ManageInitiativeInteractor manageInitiativeInteractor) {
        this.manageInitiativeInteractor = manageInitiativeInteractor;
    }

    public void addInitiative(final String name, final String createdAt, final String targetDate, final String targetTime,
                              final String description, final String targetArea, final String location, final Uri imagen, final String targetAmount,
                              final String status, final String createdBy)
    {
        if(TextUtils.isEmpty(name))
        {
            manageInitiativeInteractor.onNameEmpty();
            return;
        }
        if(TextUtils.isEmpty(location))
        {
            manageInitiativeInteractor.onLocationEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetArea))
        {
            manageInitiativeInteractor.onTargetAreaEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetDate))
        {
            manageInitiativeInteractor.onTargetDateEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetTime))
        {
            manageInitiativeInteractor.onTargetTimeEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetAmount))
        {
            manageInitiativeInteractor.onTargetAmountEmpty();
            return;
        }

        if(!CommonUtils.isInitiativeNameValid(name))
        {
            manageInitiativeInteractor.onNameFormatError();
            return;
        }

        if(!CommonUtils.isTargetDateValid(targetDate))
        {
            manageInitiativeInteractor.onTargetDateBeforeNowError();
            return;
        }

        if(Integer.parseInt(targetAmount) < 0)
        {
            manageInitiativeInteractor.onTargetAmountFormatError();
            return;
        }

        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative addInitiative = new Initiative(name, createdAt, targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, createdBy);
        repository.add(addInitiative);

        manageInitiativeInteractor.onSuccess();
    }

    public void editInitiative(final int id, final String name, final String createdAt, final String targetDate, final String targetTime,
                               final String description, final String targetArea, final String location, final Uri imagen, final String targetAmount,
                               final String status, final String createdBy)
    {
        if(TextUtils.isEmpty(location))
        {
            manageInitiativeInteractor.onLocationEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetArea))
        {
            manageInitiativeInteractor.onTargetAreaEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetDate))
        {
            manageInitiativeInteractor.onTargetDateEmpty();
            return;
        }
        if(TextUtils.isEmpty(targetTime))
        {
            manageInitiativeInteractor.onTargetTimeEmpty();
            return;
        }
        if(!CommonUtils.isTargetDateValid(targetDate))
        {
            manageInitiativeInteractor.onTargetDateBeforeNowError();
            return;
        }

        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative editInitiative = new Initiative(id, name, createdAt, targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, createdBy);

        repository.update(editInitiative);

        manageInitiativeInteractor.onSuccess();
    }

    public void deleteInitiative(Initiative initiative)
    {
        if(initiative.getUserJoined().size() >= 1)
        {
            manageInitiativeInteractor.onCannotDeleted();
            return;
        }

        InitiativeRepository repository = InitiativeRepository.getInstance();

        repository.delete(initiative);

        manageInitiativeInteractor.onSuccessDeleted();
    }
}
