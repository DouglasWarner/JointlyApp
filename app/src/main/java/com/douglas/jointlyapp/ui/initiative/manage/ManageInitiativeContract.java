package com.douglas.jointlyapp.ui.initiative.manage;

import android.net.Uri;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BasePresenter;
import com.douglas.jointlyapp.ui.base.BaseView;

public interface ManageInitiativeContract {

    interface View extends BaseView
    {
        void setNameEmpty();
        void setNameFormatError();
        void setLocationEmpty();
        void setTargetAreaEmpty();
        void setTargetDateEmpty();
        void setTargetDateBeforeNowError();
        void setTargetTimeEmpty();
        void setTargetAmountEmpty();
        void setTargetAmountFormatError();

        void setCannotDeleted();
        void setSuccessDeleted();
    }

    interface Presenter extends BasePresenter
    {
        void addInitiative(String name, String createAt, String targetDate, String targetTime, String description, String targetArea,
                           String location, Uri imagen, String targetAmount, String status, String createdBy);

        void editInitiative(int id, String name, String createAt, String targetDate, String targetTime, String description, String targetArea,
                            String location, Uri imagen, String targetAmount, String status, String createdBy);

        void delete(Initiative initiative);
    }
}
