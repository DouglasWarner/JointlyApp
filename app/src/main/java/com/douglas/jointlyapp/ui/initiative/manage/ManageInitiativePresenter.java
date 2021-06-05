package com.douglas.jointlyapp.ui.initiative.manage;

import android.graphics.Bitmap;

import com.douglas.jointlyapp.data.model.Initiative;

public class ManageInitiativePresenter implements ManageInitiativeContract.Presenter, ManageInitiativeInteractorImpl.ManageInitiativeInteractor {

    private ManageInitiativeContract.View view;
    private ManageInitiativeInteractorImpl interactor;

    public ManageInitiativePresenter(ManageInitiativeContract.View view) {
        this.view = view;
        interactor = new ManageInitiativeInteractorImpl(this);
    }

    @Override
    public void addInitiative(String name, String targetDate, String targetTime, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String created_by) {
        interactor.addInitiative(name, targetDate, targetTime, description,targetArea,location,imagen,targetAmount,status,created_by);
    }

    @Override
    public void editInitiative(long id, String name, String createAt, String targetDate, String targetTime, String description, String targetArea, String location, Bitmap imagen, String targetAmount, String status, String created_by, String ref_code) {
        interactor.editInitiative(id,name, createAt, targetDate, targetTime, description,targetArea,location,imagen,targetAmount,status,created_by, ref_code);
    }

    @Override
    public void delete(Initiative initiative) {
        interactor.deleteInitiative(initiative);
    }

    @Override
    public void onNameEmpty() {
        view.setNameEmpty();
    }

    @Override
    public void onNameFormatError() {
        view.setNameFormatError();
    }

    @Override
    public void onLocationEmpty() {
        view.setLocationEmpty();
    }

    @Override
    public void onTargetAreaEmpty() {
        view.setTargetAreaEmpty();
    }

    @Override
    public void onTargetDateEmpty() {
        view.setTargetDateEmpty();
    }

    @Override
    public void onTargetDateBeforeNowError() {
        view.setTargetDateBeforeNowError();
    }

    @Override
    public void onTargetTimeEmpty() {
        view.setTargetTimeEmpty();
    }

    @Override
    public void onTargetAmountEmpty() {
        view.setTargetAmountEmpty();
    }

    @Override
    public void onTargetAmountFormatError() {
        view.setTargetAmountFormatError();
    }

    @Override
    public void onCannotDeleted() {
        view.setCannotDeleted();
    }

    @Override
    public void onSuccessDeleted() {
        view.setSuccessDeleted();
    }

    @Override
    public void onSuccessLoad(Initiative initiative) {
        view.onSuccessLoad(initiative);
    }

    @Override
    public void onSuccess(Initiative initiative) {
        view.onSuccess(initiative);
    }

    @Override
    public void onUnsuccess(String message) {
        view.onUnsuccess(message);
    }

    @Override
    public void onError(String message) {
        view.setOnError(message);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.interactor = null;
    }
}
