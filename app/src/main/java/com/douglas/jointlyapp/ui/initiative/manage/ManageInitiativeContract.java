package com.douglas.jointlyapp.ui.initiative.manage;

import android.graphics.Bitmap;

import com.douglas.jointlyapp.data.model.Initiative;
import com.douglas.jointlyapp.ui.base.BasePresenter;

public interface ManageInitiativeContract {

    interface View {
        void setNameEmpty();
        void setNameFormatError();
        void setLocationEmpty();
        void setTargetAreaEmpty();
        void setTargetDateEmpty();
        void setTargetDateBeforeNowError();
        void setTargetTimeEmpty();
        void setTargetAmountEmpty();
        void setTargetAmountFormatError();

        void onSuccess(Initiative initiative);
        void onUnsuccess();

        void setOnError(String message);
    }

    interface Presenter extends BasePresenter {
        void addInitiative(String name, String targetDate, String targetTime, String description, String targetArea,
                           String location, Bitmap imagen, String targetAmount, String created_by);

        void editInitiative(long id, String name, String createAt, String targetDate, String targetTime, String description, String targetArea,
                            String location, Bitmap imagen, String targetAmount, String created_by, String ref_code);
    }
}
