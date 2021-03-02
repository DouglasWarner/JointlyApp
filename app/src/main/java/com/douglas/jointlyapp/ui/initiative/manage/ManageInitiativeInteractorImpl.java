package com.douglas.jointlyapp.ui.initiative.manage;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.data.model.User;
import com.douglas.jointlyapp.data.repository.InitiativeRepository;
import com.douglas.jointlyapp.data.repository.UserRepository;
import com.douglas.jointlyapp.ui.utils.CommonUtils;

import java.util.List;

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

        void onSuccessLoad(Initiative initiative);
        void onSuccess(Initiative initiative);
    }

    private ManageInitiativeInteractor interactor;

    public ManageInitiativeInteractorImpl(ManageInitiativeInteractor manageInitiativeInteractor) {
        this.interactor = manageInitiativeInteractor;
    }

    public void addInitiative(final String name, final String targetDate, final String targetTime,
                              final String description, final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                              final String status, final String createdBy)
    {
        if (isNotValidInitiative(name, targetDate, targetTime, targetArea, location, targetAmount))
            return;

        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative addInitiative = new Initiative(name, CommonUtils.getDateNow(), targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, createdBy);

        int result = (int) repository.insert(addInitiative);

        Initiative initiative = repository.getInitiative(result);

        interactor.onSuccess(initiative);
    }

    public void editInitiative(final int id, final String name, final String createAt, final String targetDate, final String targetTime,
                               final String description, final String targetArea, final String location, final Bitmap imagen, final String targetAmount,
                               final String status, final String createdBy)
    {
        if (isNotValidInitiative(targetDate, targetTime, targetArea, location))
            return;

        InitiativeRepository repository = InitiativeRepository.getInstance();

        Initiative editInitiative = new Initiative(id, name, createAt, targetDate, targetTime, description, targetArea, location, imagen, targetAmount, status, createdBy);

        repository.update(editInitiative);

        Initiative initiative = repository.getInitiative(editInitiative.getId());

        interactor.onSuccess(initiative);
    }

    public void deleteInitiative(final Initiative initiative)
    {
        List<User> userList = UserRepository.getInstance().getListUserJoined(initiative.getId());

        if(!userList.isEmpty())
        {
            interactor.onCannotDeleted();
            return;
        }

        InitiativeRepository repository = InitiativeRepository.getInstance();

        repository.delete(initiative);

        interactor.onSuccessDeleted();
    }

    public void loadInitiative(final int idInitiative)
    {
        new Handler().postDelayed(() -> {

            Initiative initiative = InitiativeRepository.getInstance().getInitiative(idInitiative);

            interactor.onSuccessLoad(initiative);
        },300);
    }

    private boolean isNotValidInitiative(String name, String targetDate, String targetTime, String targetArea, String location, String targetAmount) {
        if(TextUtils.isEmpty(name))
        {
            interactor.onNameEmpty();
            return true;
        }
        if(TextUtils.isEmpty(location))
        {
            interactor.onLocationEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetArea))
        {
            interactor.onTargetAreaEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetDate))
        {
            interactor.onTargetDateEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetTime))
        {
            interactor.onTargetTimeEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetAmount))
        {
            interactor.onTargetAmountEmpty();
            return true;
        }

        if(!CommonUtils.isInitiativeNameValid(name))
        {
            interactor.onNameFormatError();
            return true;
        }

//        if(!CommonUtils.isTargetDateValid(targetDate))
//        {
//            interactor.onTargetDateBeforeNowError();
//            return true;
//        }

        if(Integer.parseInt(targetAmount) < 0)
        {
            interactor.onTargetAmountFormatError();
            return true;
        }
        return false;
    }

    private boolean isNotValidInitiative(String targetDate, String targetTime, String targetArea, String location) {
        if(TextUtils.isEmpty(location))
        {
            interactor.onLocationEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetArea))
        {
            interactor.onTargetAreaEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetDate))
        {
            interactor.onTargetDateEmpty();
            return true;
        }
        if(TextUtils.isEmpty(targetTime))
        {
            interactor.onTargetTimeEmpty();
            return true;
        }
        if(!CommonUtils.isTargetDateValid(targetDate))
        {
            interactor.onTargetDateBeforeNowError();
            return true;
        }
        return false;
    }
}
